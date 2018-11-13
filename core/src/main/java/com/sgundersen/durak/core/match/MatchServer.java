package com.sgundersen.durak.core.match;

import com.sgundersen.durak.core.net.MatchConfiguration;
import com.sgundersen.durak.core.net.MatchClientState;
import com.sgundersen.durak.core.net.PlayerAction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchServer {

    @Getter
    private final int id;

    @Getter
    private final MatchConfiguration configuration;

    private final Talon talon;
    private final Bout bout;
    private final DiscardPile discardPile = new DiscardPile();
    private final Map<Integer, Hand> hands = new HashMap<>();
    private int handIdCounter = 0;

    private int attackingPlayerIndex = -1;
    private int defendingPlayerIndex = -1;

    @Getter
    private boolean started = false;

    public MatchServer(int id, MatchConfiguration configuration) {
        this.id = id;
        this.configuration = configuration;
        talon = new Talon(configuration.getLowestRank());
        bout = new Bout(talon.getBottom().getSuit());
    }

    public int getPlayerCount() {
        return hands.size();
    }

    public boolean isAcceptingPlayers() {
        return configuration.getMaxPlayers() > hands.size() && !started;
    }

    public int addPlayer() {
        if (!isAcceptingPlayers()) {
            return -1;
        }
        handIdCounter++;
        hands.put(handIdCounter, new Hand());
        return handIdCounter;
    }

    public void removePlayer(int index) {
        hands.remove(index);
    }

    /**
     * The attacker must take cards first, followed by other players lacking cards (means they joined the attack)
     * Finally, the defender can take the needed cards.
     */
    private void deal() {
        Hand attacker = hands.get(attackingPlayerIndex);
        Hand defender = hands.get(defendingPlayerIndex);
        attacker.addNeededCards(talon);
        for (Hand hand : hands.values()) {
            if (hand != attacker && hand != defender) {
                hand.addNeededCards(talon);
            }
        }
        defender.addNeededCards(talon);
    }

    public void start() {
        if (2 > hands.size()) {
            System.err.println("Cannot start a match with less than 2 players.");
            return;
        }
        if (!started) {
            started = true;
            setAttackerAndDefender();
            deal();
        }
    }

    // TODO: This will currently only switch between two players... Support for 3+ players should be added.
    // TODO: Probably need to rework how players are stored in here. Reconsider use of list.
    private void setAttackerAndDefender() {
        int previousAttackingPlayerIndex = attackingPlayerIndex;
        int previousDefendingPlayerIndex = defendingPlayerIndex;
        for (int key : hands.keySet()) {
            if (previousAttackingPlayerIndex != key && previousAttackingPlayerIndex == attackingPlayerIndex) {
                attackingPlayerIndex = key;
            } else if (previousDefendingPlayerIndex != key && previousDefendingPlayerIndex == defendingPlayerIndex) {
                defendingPlayerIndex = key;
            }
        }
    }

    private void onTurnEnding() {
        if (bout.isAttackerPresent()) {
            System.err.println("Cannot end turn while an attacking card is present.");
            return;
        }
        discardPile.addAll(bout.takeCards());
        deal();
        setAttackerAndDefender();
    }

    private void onAttackingPlayerAction(PlayerAction action) {
        if (action.isTakingCards()) {
            System.err.println("Attacker cannot take cards");
        } else if (action.isEndingTurn()) {
            onTurnEnding();
        } else {
            Hand hand = hands.get(attackingPlayerIndex);
            Card attackingCard = hand.get(action.getCardIndex());
            if (attackingCard == null) {
                System.err.println("This card does not exist");
                return;
            }
            if (bout.canAttack(attackingCard)) {
                bout.attack(attackingCard);
                hand.take(action.getCardIndex());
            }
        }
    }

    private void onDefendingPlayerAction(PlayerAction action) {
        Hand hand = hands.get(defendingPlayerIndex);
        if (action.isTakingCards()) {
            if (bout.isAttackerPresent()) {
                hand.addAll(bout.takeCards());
            } else {
                System.err.println("Cannot take cards while no attacking card is present");
            }
        } else {
            Card defendingCard = hand.get(action.getCardIndex());
            if (defendingCard == null) {
                System.err.println("This card does not exist");
                return;
            }
            if (bout.canDefend(defendingCard)) {
                bout.defend(defendingCard);
                hand.take(action.getCardIndex());
                if (hand.isEmpty() || hands.get(attackingPlayerIndex).isEmpty()) {
                    onTurnEnding();
                }
            }
        }
    }

    public void processAction(int playerIndex, PlayerAction action) {
        if (attackingPlayerIndex == playerIndex) {
            onAttackingPlayerAction(action);
        } else if (defendingPlayerIndex == playerIndex) {
            onDefendingPlayerAction(action);
        } else {
            System.err.println("Player is not currently attacking or defending");
        }
    }

    private List<Hand> getEmptyHands() {
        List<Hand> emptyHands = new ArrayList<>();
        for (Hand hand : hands.values()) {
            if (hand.isEmpty()) {
                emptyHands.add(hand);
            }
        }
        return emptyHands;
    }

    private MatchOutcome getOutcome(int playerIndex) {
        List<Hand> winningHands = getEmptyHands();
        if (winningHands.isEmpty() || bout.isAttackerPresent() || talon.count() > 0) {
            return MatchOutcome.NotYetDecided;
        }
        for (Hand winningHand : winningHands) {
            if (winningHand == hands.get(playerIndex)) {
                boolean isDraw = winningHands.size() > 1;
                return isDraw ? MatchOutcome.Draw : MatchOutcome.Victory;
            }
        }
        return MatchOutcome.Defeat;
    }

    public MatchClientState getClientState(int playerIndex) {
        Hand hand = hands.get(playerIndex);
        if (hand == null) {
            return null;
        }
        MatchClientState state = new MatchClientState();
        state.setPlayerCount(hands.size());
        state.setTalonCardCount(talon.count());
        state.setDiscardPileCount(discardPile.count());
        state.setBottomCard(talon.getBottom());
        if (playerIndex == defendingPlayerIndex && attackingPlayerIndex != -1) {
            state.setOtherPlayerHandCount(hands.get(attackingPlayerIndex).count());
        } else if (playerIndex == attackingPlayerIndex && defendingPlayerIndex != -1) {
            state.setOtherPlayerHandCount(hands.get(defendingPlayerIndex).count());
        }
        state.setHand(hand);
        state.setBout(bout);
        state.setAttacking(attackingPlayerIndex == playerIndex);
        state.setDefending(defendingPlayerIndex == playerIndex);
        state.setOutcome(getOutcome(playerIndex));
        return state;
    }

}
