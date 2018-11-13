package com.sgundersen.durak.core.match;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bout {

    private static final int MAX_ATTACKING_CARDS = 6;

    private List<Card> finishedCards = new ArrayList<>();
    private CardSuit trumpingCardSuit;
    private Card attackingCard;
    private int attackCount = 0;

    public Bout(CardSuit trumpingCardSuit) {
        this.trumpingCardSuit = trumpingCardSuit;
    }

    public boolean canAttack(Card attackingCard) {
        if (isAttackerPresent()) {
            return false;
        }
        if (finishedCards.isEmpty()) {
            return true;
        }
        if (attackCount >= MAX_ATTACKING_CARDS) {
            return false;
        }
        for (Card finishedCard : finishedCards) {
            if (finishedCard.getRank() == attackingCard.getRank()) {
                return true;
            }
        }
        return false;
    }

    public boolean canDefend(Card defendingCard) {
        if (!isAttackerPresent()) {
            return false;
        }
        if (defendingCard.getSuit() == trumpingCardSuit && attackingCard.getSuit() != trumpingCardSuit) {
            return true;
        }
        if (defendingCard.getSuit() == attackingCard.getSuit() && defendingCard.getRank() > attackingCard.getRank()) {
            return true;
        }
        return false;
    }

    public void attack(Card attackingCard) {
        if (canAttack(attackingCard)) {
            this.attackingCard = attackingCard;
            attackCount++;
        }
    }

    public void defend(Card defendingCard) {
        if (canDefend(defendingCard)) {
            finishedCards.add(attackingCard);
            finishedCards.add(defendingCard);
            attackingCard = null;
        }
    }

    public void reset() {
        finishedCards.clear();
        attackingCard = null;
        attackCount = 0;
    }

    public boolean isAttackerPresent() {
        return attackingCard != null;
    }

    public List<Card> takeCards() {
        List<Card> cards = new ArrayList<>(finishedCards);
        if (attackingCard != null) {
            cards.add(attackingCard);
        }
        reset();
        return cards;
    }

}
