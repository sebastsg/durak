package com.sgundersen.durak.core.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Card {

    private CardSuit suit;
    private int rank;

    public String getRankName() {
        switch (rank) {
            case 2: return "Two";
            case 3: return "Three";
            case 4: return "Four";
            case 5: return "Five";
            case 6: return "Six";
            case 7: return "Seven";
            case 8: return "Eight";
            case 9: return "Nine";
            case 10: return "Ten";
            case 11: return "Jack";
            case 12: return "Queen";
            case 13: return "King";
            case 14: return "Ace";
            default: return "Invalid Card";
        }
    }

    public String getFullName() {
        return getRankName() + " of " + suit.toString();
    }

    public int getIndex() {
        return suit.ordinal() * 13 + rank - 2;
    }

}
