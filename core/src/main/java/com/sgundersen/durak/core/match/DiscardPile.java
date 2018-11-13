package com.sgundersen.durak.core.match;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {

    private List<Card> cards = new ArrayList<>();

    public void add(Card card) {
        cards.add(card);
    }

    public void addAll(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public int count() {
        return cards.size();
    }

}
