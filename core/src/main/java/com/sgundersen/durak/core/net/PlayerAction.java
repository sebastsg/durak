package com.sgundersen.durak.core.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAction {

    private int cardIndex;
    private boolean takingCards;
    private boolean endingTurn;

    public static PlayerAction useCard(int cardIndex) {
        return new PlayerAction(cardIndex, false, false);
    }

    public static PlayerAction takeCards() {
        return new PlayerAction(-1, true, false);
    }

    public static PlayerAction endTurn() {
        return new PlayerAction(-1, false, true);
    }

}
