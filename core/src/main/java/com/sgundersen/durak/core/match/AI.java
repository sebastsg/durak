package com.sgundersen.durak.core.match;

import com.sgundersen.durak.core.net.match.Action;
import com.sgundersen.durak.core.net.match.MatchClientState;

import java.util.ArrayList;
import java.util.List;

public class AI {

    private static Card getWeakest(List<Card> cards) {
        if (cards.isEmpty()) {
            return null;
        }
        Card weakest = cards.get(0);
        for (Card card : cards) {
            if (weakest.getRank() > card.getRank()) {
                weakest = card;
            }
        }
        return weakest;
    }

    private static List<Card> getUsable(MatchClientState state, boolean forAttack, boolean trump) {
        List<Card> usable = new ArrayList<>();
        for (Card card : state.getHand().getCards()) {
            if (forAttack && !state.getBout().canAttack(card)) {
                continue;
            }
            if (!forAttack && !state.getBout().canDefend(card)) {
                continue;
            }
            boolean isTrump = card.getSuit() == state.getTrumpingCardSuit();
            if ((trump && isTrump) || (!trump && isTrump)) {
                usable.add(card);
            }
        }
        return usable;
    }

    private static Action nextAttackAction(MatchClientState state) {
        Bout bout = state.getBout();
        Hand hand = state.getHand();
        if (bout.isAttackerPresent()) {
            return Action.endTurn();
        }
        List<Card> withoutTrump = getUsable(state, true, false);
        if (!withoutTrump.isEmpty()) {
            return Action.useCard(hand.indexOf(getWeakest(withoutTrump)));
        }
        List<Card> withTrump = getUsable(state, true, true);
        Card weakest = getWeakest(withTrump);
        if (weakest != null) {
            if (weakest.getRank() > 10 && bout.getFinishedCards().size() <= 4) {
                return Action.takeCards();
            }
            return Action.useCard(hand.indexOf(weakest));
        }
        return Action.endTurn();
    }

    private static Action nextDefendAction(MatchClientState state) {
        Bout bout = state.getBout();
        Hand hand = state.getHand();
        if (!bout.isAttackerPresent()) {
            return Action.endTurn();
        }
        List<Card> withoutTrump = getUsable(state, false, false);
        if (!withoutTrump.isEmpty()) {
            return Action.useCard(hand.indexOf(getWeakest(withoutTrump)));
        }
        List<Card> withTrump = getUsable(state, false, true);
        Card weakest = getWeakest(withTrump);
        if (weakest == null) {
            return Action.takeCards();
        }
        if (weakest.getRank() > 10 && bout.getFinishedCards().size() <= 4) {
            return Action.takeCards();
        }
        return Action.useCard(hand.indexOf(weakest));
    }

    public static Action nextAction(MatchClientState state) {
        if (state.isAttacking()) {
            return nextAttackAction(state);
        } else if (state.isDefending()) {
            return nextDefendAction(state);
        } else {
            return Action.endTurn();
        }
    }

}
