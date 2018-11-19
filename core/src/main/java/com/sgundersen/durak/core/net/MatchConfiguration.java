package com.sgundersen.durak.core.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchConfiguration {

    private String name;
    private int lowestRank;
    private int maxPlayers;

    public boolean isValid() {
        return maxPlayers > 1 && maxPlayers < 7 && lowestRank > 1 && lowestRank < 9;
    }

}
