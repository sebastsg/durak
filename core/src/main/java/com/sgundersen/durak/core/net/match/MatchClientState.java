package com.sgundersen.durak.core.net.match;

import com.sgundersen.durak.core.match.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchClientState {

    private int playerCount;
    private int talonCardCount;
    private int discardPileCount;
    private int otherPlayerHandCount;
    private CardSuit trumpingCardSuit;
    private Card bottomCard;
    private Hand hand;
    private Bout bout;
    private boolean attacking;
    private boolean defending;
    private MatchOutcome outcome;

}
