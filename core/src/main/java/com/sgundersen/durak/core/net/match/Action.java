package com.sgundersen.durak.core.net.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action {

    private int cardIndex;
    private boolean takingCards;
    private boolean endingTurn;

    public static Action useCard(int cardIndex) {
        return new Action(cardIndex, false, false);
    }

    public static Action takeCards() {
        return new Action(-1, true, false);
    }

    public static Action endTurn() {
        return new Action(-1, false, true);
    }

}
