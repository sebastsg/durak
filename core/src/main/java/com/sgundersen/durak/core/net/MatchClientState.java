package com.sgundersen.durak.core.net;

import com.sgundersen.durak.core.match.Bout;
import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.core.match.Hand;
import com.sgundersen.durak.core.match.MatchOutcome;
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
    private Card bottomCard;
    private Hand hand;
    private Bout bout;
    private boolean attacking;
    private boolean defending;
    private MatchOutcome outcome;

}
