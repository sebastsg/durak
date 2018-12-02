package com.sgundersen.durak.core.match;

import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.core.net.match.Action;
import com.sgundersen.durak.core.net.match.MatchSnapshot;
import lombok.Getter;

import java.util.*;

public class MatchServer {

    @Getter
    private final long id;

    @Getter
    private final MatchConfiguration configuration;

    private final Talon talon;
    private final Bout bout;
    private final DiscardPile discardPile = new DiscardPile();
    private final Map<Long, Hand> hands = new HashMap<>();

    private long attackingPlayerId = -1;
    private long defendingPlayerId = -1;

    public MatchServer(long id, MatchConfiguration configuration, List<Long> handIds) {
        this.id = id;
        this.configuration = configuration;
        talon = new Talon(configuration.getLowestRank());
        bout = new Bout(talon.getBottom().getSuit());
        if (2 > handIds.size()) {
            return;
        }
        attackingPlayerId = handIds.get(0);
        defendingPlayerId = handIds.get(1);
        for (long handId : handIds) {
            hands.put(handId, new Hand());
        }
    }

    public int getHandCount() {
        return hands.size();
    }

    /**
     * The attacker must take cards first, followed by other players lacking cards (means they joined the attack)
     * Finally, the defender can take the needed cards.
     */
    private void deal() {
        Hand attacker = hands.get(attackingPlayerId);
        Hand defender = hands.get(defendingPlayerId);
        attacker.addNeededCards(talon);
        for (Hand hand : hands.values()) {
            if (hand != attacker && hand != defender) {
                hand.addNeededCards(talon);
            }
        }
        defender.addNeededCards(talon);
    }

    public void start() {
        setAttackerAndDefender();
        deal();
    }

    // TODO: Make it work for 3+ players
    private void setAttackerAndDefender() {
        long currentAttackingPlayerId = attackingPlayerId;
        attackingPlayerId = defendingPlayerId;
        defendingPlayerId = currentAttackingPlayerId;
    }

    private void onTurnEnding() {
        if (bout.isAttackerPresent()) {
            return;
        }
        discardPile.addAll(bout.takeCards());
        deal();
        setAttackerAndDefender();
    }

    private void onAttackerUsingCard(int cardIndex) {
        Hand attacker = hands.get(attackingPlayerId);
        Card attackingCard = attacker.get(cardIndex);
        if (attackingCard == null) {
            return;
        }
        if (!bout.canAttack(attackingCard)) {
            return;
        }
        bout.attack(attackingCard);
        attacker.take(cardIndex);
    }

    private void onAttackAction(Action action) {
        if (action.isTakingCards()) {
            return; // Not allowed.
        }
        if (action.isEndingTurn()) {
            onTurnEnding();
        } else {
            onAttackerUsingCard(action.getCardIndex());
        }
    }

    private void onDefenderTakingCards() {
        if (!bout.isAttackerPresent()) {
            return;
        }
        Hand defender = hands.get(defendingPlayerId);
        defender.addAll(bout.takeCards());
        // The turn ends, but the defender keeps his role until the attack is over.
        // TODO: This might need to be handled differently in 3+ player games.
        deal();
    }

    private void onDefenderUsingCard(int cardIndex) {
        Hand defender = hands.get(defendingPlayerId);
        Card defendingCard = defender.get(cardIndex);
        if (defendingCard == null) {
            return;
        }
        if (!bout.canDefend(defendingCard)) {
            return;
        }
        bout.defend(defendingCard);
        defender.take(cardIndex);
        if (defender.isEmpty() || hands.get(attackingPlayerId).isEmpty()) {
            onTurnEnding();
        }
    }

    private void onDefendAction(Action action) {
        if (action.isTakingCards()) {
            onDefenderTakingCards();
        } else {
            onDefenderUsingCard(action.getCardIndex());
        }
    }

    public void onAction(long playerId, Action action) {
        if (attackingPlayerId == playerId) {
            onAttackAction(action);
        } else if (defendingPlayerId == playerId) {
            onDefendAction(action);
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

    private MatchOutcome getOutcome(long playerId) {
        List<Hand> winningHands = getEmptyHands();
        if (winningHands.isEmpty() || bout.isAttackerPresent() || talon.count() > 0) {
            return MatchOutcome.NotYetDecided;
        }
        for (Hand winningHand : winningHands) {
            if (winningHand == hands.get(playerId)) {
                boolean isDraw = winningHands.size() > 1;
                return isDraw ? MatchOutcome.Draw : MatchOutcome.Victory;
            }
        }
        return MatchOutcome.Defeat;
    }

    public MatchClientState getClientState(long playerId) {
        Hand hand = hands.get(playerId);
        if (hand == null) {
            return null;
        }
        MatchClientState state = new MatchClientState();
        state.setPlayerCount(hands.size());
        state.setTalonCardCount(talon.count());
        state.setDiscardPileCount(discardPile.count());
        state.setBottomCard(talon.getBottom());
        if (playerId == defendingPlayerId && attackingPlayerId != -1) {
            state.setOtherPlayerHandCount(hands.get(attackingPlayerId).count());
        } else if (playerId == attackingPlayerId && defendingPlayerId != -1) {
            state.setOtherPlayerHandCount(hands.get(defendingPlayerId).count());
        }
        state.setHand(hand);
        state.setBout(bout);
        state.setAttacking(attackingPlayerId == playerId);
        state.setDefending(defendingPlayerId == playerId);
        state.setOutcome(getOutcome(playerId));
        return state;
    }

    public MatchSnapshot getSnapshot() {
        MatchSnapshot snapshot = new MatchSnapshot();
        for (long playerId : hands.keySet()) {
            snapshot.getClientStates().add(getClientState(playerId));
        }
        return snapshot;
    }

}
