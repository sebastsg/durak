package com.sgundersen.durak.core.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchConfiguration {

    private String name = "New match";
    private int lowestRank = 6;
    private int maxPlayers = 2;
    private int secondsPerTurn = 10;

    public boolean isLowestRankValid() {
        return lowestRank > 1 && lowestRank < 9;
    }

    public boolean isMaxPlayersValid() {
        return maxPlayers > 1 && maxPlayers < 7;
    }

    public boolean isSecondsPerTurnValid() {
        return secondsPerTurn > 5 && secondsPerTurn < 180;
    }

    public boolean isValid() {
        return isLowestRankValid() && isMaxPlayersValid() && isSecondsPerTurnValid();
    }

}
