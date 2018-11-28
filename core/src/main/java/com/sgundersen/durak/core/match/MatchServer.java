package com.sgundersen.durak.core.match;

import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.core.net.match.Action;
import lombok.Getter;

import java.util.*;

public class MatchServer {

    @Getter
    private final int id;

    @Getter
    private final MatchConfiguration configuration;

    private final Talon talon;
    private final Bout bout;
    private final DiscardPile discardPile = new DiscardPile();
    private final Map<Integer, Hand> hands = new HashMap<>();

    private int attackingHandId = -1;
    private int defendingHandId = -1;

    public MatchServer(int id, MatchConfiguration configuration, Set<Integer> handIds) {
        this.id = id;
        this.configuration = configuration;
        for (int handId : handIds) {
            hands.put(handId, new Hand());
        }
        talon = new Talon(configuration.getLowestRank());
        bout = new Bout(talon.getBottom().getSuit());
    }

    public int getHandCount() {
        return hands.size();
    }

    /**
     * The attacker must take cards first, followed by other players lacking cards (means they joined the attack)
     * Finally, the defender can take the needed cards.
     */
    private void deal() {
        Hand attacker = hands.get(attackingHandId);
        Hand defender = hands.get(defendingHandId);
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
        setAttackerAndDefender();
        deal();
    }

    // TODO: This will currently only switch between two players... Support for 3+ players should be added.
    // TODO: Probably need to rework how players are stored in here. Reconsider use of list.
    private void setAttackerAndDefender() {
        int previousAttackingPlayerIndex = attackingHandId;
        int previousDefendingPlayerIndex = defendingHandId;
        for (int key : hands.keySet()) {
            if (previousAttackingPlayerIndex != key && previousAttackingPlayerIndex == attackingHandId) {
                attackingHandId = key;
            } else if (previousDefendingPlayerIndex != key && previousDefendingPlayerIndex == defendingHandId) {
                defendingHandId = key;
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

    private void onAttackingHandAction(Action action) {
        if (action.isTakingCards()) {
            System.err.println("Attacker cannot take cards");
        } else if (action.isEndingTurn()) {
            onTurnEnding();
        } else {
            Hand hand = hands.get(attackingHandId);
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

    private void onDefendingHandAction(Action action) {
        Hand hand = hands.get(defendingHandId);
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
                if (hand.isEmpty() || hands.get(attackingHandId).isEmpty()) {
                    onTurnEnding();
                }
            }
        }
    }

    public void processAction(int handId, Action action) {
        if (attackingHandId == handId) {
            onAttackingHandAction(action);
        } else if (defendingHandId == handId) {
            onDefendingHandAction(action);
        } else {
            System.err.println("This hand is not currently attacking or defending");
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

    public MatchClientState getClientState(int handId) {
        Hand hand = hands.get(handId);
        if (hand == null) {
            return null;
        }
        MatchClientState state = new MatchClientState();
        state.setPlayerCount(hands.size());
        state.setTalonCardCount(talon.count());
        state.setDiscardPileCount(discardPile.count());
        state.setBottomCard(talon.getBottom());
        if (handId == defendingHandId && attackingHandId != -1) {
            state.setOtherPlayerHandCount(hands.get(attackingHandId).count());
        } else if (handId == attackingHandId && defendingHandId != -1) {
            state.setOtherPlayerHandCount(hands.get(defendingHandId).count());
        }
        state.setHand(hand);
        state.setBout(bout);
        state.setAttacking(attackingHandId == handId);
        state.setDefending(defendingHandId == handId);
        state.setOutcome(getOutcome(handId));
        return state;
    }

    public List<MatchClientState> getAllClientStates() {
        List<MatchClientState> states = new ArrayList<>();
        for (int handId : hands.keySet()) {
            states.add(getClientState(handId));
        }
        return states;
    }

}
