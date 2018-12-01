package com.sgundersen.durak.server.service;

import com.sgundersen.durak.core.match.MatchOutcome;
import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.net.match.Action;
import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.server.db.*;
import com.sgundersen.durak.server.lobby.Lobby;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("match")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class MatchService {

    private static final Jsonb jsonb = JsonbBuilder.create();
    private static final Map<Long, MatchServer> servers = new ConcurrentHashMap<>();

    private static PlayerEntity getPlayer(HttpServletRequest request) {
        return request == null ? null : PlayerService.getPlayer(request.getSession().getId());
    }

    public static MatchServer getServer(PlayerEntity player) {
        return player == null ? null : servers.get(player.getMatchId());
    }

    public static boolean start(Lobby lobby) {
        if (lobby == null) {
            return false;
        }
        MatchServer server = new MatchServer(lobby.getId(), lobby.getConfiguration(), lobby.getPlayerIds());
        server.start();
        servers.put(lobby.getId(), server);
        return true;
    }

    @Inject
    private PlayerDao playerDao;

    @Inject
    private MatchDao matchDao;

    @GET
    @Path("state")
    public String state(@Context HttpServletRequest request) {
        PlayerEntity player = getPlayer(request);
        MatchServer server = getServer(player);
        if (server == null) {
            return "";
        }
        MatchClientState state = server.getClientState(player.getId());
        if (state == null) {
            return "";
        }
        return jsonb.toJson(state);
    }

    @POST
    @Path("action")
    @Consumes(MediaType.APPLICATION_JSON)
    public String action(@Context HttpServletRequest request, String json) {
        PlayerEntity player = getPlayer(request);
        MatchServer server = getServer(player);
        if (server == null) {
            return "";
        }
        Action action = jsonb.fromJson(json, Action.class);
        if (action == null) {
            return "";
        }
        server.onAction(player.getId(), action);
        saveSnapshot(server);
        checkOutcomeForPlayers(server);
        return jsonb.toJson(server.getClientState(player.getId()));
    }

    private void checkOutcomeForPlayers(MatchServer server) {
        List<PlayerEntity> players = PlayerService.getPlayersInMatch(server.getId());
        for (PlayerEntity player : players) {
            checkOutcome(server, player);
        }
    }

    private void checkOutcome(MatchServer server, PlayerEntity player) {
        MatchOutcome outcome = server.getClientState(player.getId()).getOutcome();
        if (outcome == MatchOutcome.Victory) {
            player.onVictory();
            playerDao.save(player);
        } else if (outcome == MatchOutcome.Defeat) {
            player.onDefeat();
            playerDao.save(player);
        }
    }

    private void saveSnapshot(MatchServer server) {
        MatchEntity match = matchDao.find(server.getId());
        if (match != null) {
            match.addSnapshot(server.getSnapshot());
            matchDao.save(match);
        }
    }

}
