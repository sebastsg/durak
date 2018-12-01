package com.sgundersen.durak.server.lobby;

import com.sgundersen.durak.core.match.MatchConfiguration;
import com.sgundersen.durak.server.db.PlayerEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Lobby {

    private final long id;
    private final MatchConfiguration configuration = new MatchConfiguration();
    private final List<PlayerEntity> players = new ArrayList<>();

    public Lobby(long id, String name) {
        this.id = id;
        configuration.setName(name);
    }

    public List<Long> getPlayerIds() {
        List<Long> ids = new ArrayList<>();
        for (PlayerEntity player : players) {
            ids.add(player.getId());
        }
        return ids;
    }

    public void addPlayer(PlayerEntity player) {
        player.setMatchId(id);
        players.add(player);
    }

    public void removePlayer(PlayerEntity player) {
        players.remove(player);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean isAcceptingPlayers() {
        return configuration.getMaxPlayers() > players.size();
    }

}
