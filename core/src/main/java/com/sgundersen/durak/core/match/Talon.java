package com.sgundersen.durak.core.match;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Talon {

    private static final int CARDS_PER_SUIT = 13;

    @Getter
    private final int lowestRank;

    private final List<Card> cards = new ArrayList<>();

    public Talon(int lowestRank) {
        if (lowestRank < 2) {
            lowestRank = 2;
        }
        this.lowestRank = lowestRank;
        addSuit(CardSuit.Hearts);
        addSuit(CardSuit.Diamonds);
        addSuit(CardSuit.Clovers);
        addSuit(CardSuit.Spades);
        shuffle();
    }

    private void addSuit(CardSuit suit) {
        for (int rank = lowestRank; rank < 2 + CARDS_PER_SUIT; rank++) {
            cards.add(new Card(suit, rank));
        }
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    public Card take() {
        if (cards.isEmpty()) {
            return null;
        }
        Card card = cards.get(cards.size() - 1);
        cards.remove(cards.size() - 1);
        return card;
    }

    public int count() {
        return cards.size();
    }

    public Card getBottom() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }

}
