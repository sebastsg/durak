package com.sgundersen.durak.core.match;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hand {

    private List<Card> cards = new ArrayList<>();

    public void add(Card card) {
        cards.add(card);
    }

    public void addAll(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card get(int index) {
        try {
            return cards.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Card take(int index) {
        try {
            Card card = cards.get(index);
            cards.remove(index);
            return card;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Card " + index + " was not found. Stack trace: "  + e.getMessage());
            return null;
        }
    }

    public Card take(Card card) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) == card) {
                return take(i);
            }
        }
        return null;
    }

    public void addNeededCards(Talon talon) {
        while (6 > count() && talon.count() > 0) {
            add(talon.take());
        }
    }

    public int count() {
        return cards.size();
    }

}
