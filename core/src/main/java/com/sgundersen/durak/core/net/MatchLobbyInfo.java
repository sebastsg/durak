package com.sgundersen.durak.core.net;

import com.sgundersen.durak.core.match.MatchServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchLobbyInfo {

    private int id;
    private String name;
    private int maxPlayers;
    private int currentPlayers;

    public MatchLobbyInfo(int id, int currentPlayers, MatchConfiguration configuration) {
        this.id = id;
        name = configuration.getName();
        maxPlayers = configuration.getMaxPlayers();
        this.currentPlayers = currentPlayers;
    }

    public MatchLobbyInfo(MatchServer server) {
        this(server.getId(), server.getPlayerCount(), server.getConfiguration());
    }

}
