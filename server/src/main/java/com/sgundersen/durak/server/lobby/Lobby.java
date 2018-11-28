package com.sgundersen.durak.server.lobby;

import com.sgundersen.durak.core.match.MatchConfiguration;
import com.sgundersen.durak.server.match.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Getter
public class Lobby {

    private final int id;
    private final MatchConfiguration configuration;
    private int playerIdCounter = 0;
    private static final Map<Integer, Player> players = new ConcurrentHashMap<>();

    public Set<Integer> getHandIds() {
        return players.keySet();
    }

    public int addPlayer(Player player) {
        if (player == null) {
            return -1;
        }
        playerIdCounter++;
        players.put(playerIdCounter, player);
        return playerIdCounter;
    }

    public void removePlayer(Player player) {
        players.remove(player.getHandId());
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean isAcceptingPlayers() {
        return configuration.getMaxPlayers() > players.size();
    }

}
