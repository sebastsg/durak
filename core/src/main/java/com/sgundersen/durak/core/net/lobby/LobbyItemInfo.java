package com.sgundersen.durak.core.net.lobby;

import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.match.MatchConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LobbyItemInfo {

    private int id = -1;
    private String name = "New match";
    private int maxPlayers = 2;
    private int currentPlayers = 0;
    private int secondsPerTurn = 10;

    public LobbyItemInfo(int id, int currentPlayers, MatchConfiguration configuration) {
        this.id = id;
        name = configuration.getName();
        maxPlayers = configuration.getMaxPlayers();
        secondsPerTurn = configuration.getSecondsPerTurn();
        this.currentPlayers = currentPlayers;
    }

    public LobbyItemInfo(MatchServer server) {
        this(server.getId(), server.getHandCount(), server.getConfiguration());
    }

}
