package com.sgundersen.durak.server.service;

import com.sgundersen.durak.core.match.MatchOutcome;
import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.net.match.Action;
import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.server.lobby.Lobby;
import com.sgundersen.durak.server.match.Player;
import com.sgundersen.durak.server.db.PlayerProfile;
import com.sgundersen.durak.server.db.PlayerProfileDao;
import com.sgundersen.durak.server.db.RecordedMatch;
import com.sgundersen.durak.server.db.RecordedMatchDao;

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
    private static final Map<Integer, MatchServer> servers = new ConcurrentHashMap<>();

    private static Player getPlayer(HttpServletRequest request) {
        return request == null ? null : PlayerService.getPlayer(request.getSession().getId());
    }

    public static MatchServer getServer(Player player) {
        return player == null ? null : servers.get(player.getMatchId());
    }

    public static boolean start(Lobby lobby) {
        if (lobby == null) {
            return false;
        }
        MatchServer server = new MatchServer(lobby.getId(), lobby.getConfiguration(), lobby.getHandIds());
        server.start();
        servers.put(lobby.getId(), server);
        return true;
    }

    @Inject
    private PlayerProfileDao playerProfileDao;

    @Inject
    private RecordedMatchDao recordedMatchDao;

    @GET
    @Path("state")
    public String state(@Context HttpServletRequest request) {
        Player player = getPlayer(request);
        MatchServer server = getServer(player);
        if (server == null) {
            return "";
        }
        MatchClientState state = server.getClientState(player.getHandId());
        if (state == null) {
            return "";
        }
        return jsonb.toJson(state);
    }

    @POST
    @Path("action")
    @Consumes(MediaType.APPLICATION_JSON)
    public String action(@Context HttpServletRequest request, String json) {
        Player player = getPlayer(request);
        MatchServer server = getServer(player);
        if (server == null) {
            return "";
        }
        Action action = jsonb.fromJson(json, Action.class);
        if (action == null) {
            return "";
        }
        int handId = player.getHandId();
        server.processAction(handId, action);
        recordMatchState(server);
        checkOutcomeForPlayers(server);
        return jsonb.toJson(server.getClientState(handId));
    }

    @GET
    @Path("recordings")
    public String recordings() {
        return jsonb.toJson(recordedMatchDao.getAllMeta());
    }

    @GET
    @Path("recording/{id}")
    public String recording(@PathParam("id") int matchId) {
        RecordedMatch recordedMatch = recordedMatchDao.find(matchId);
        if (recordedMatch == null) {
            return "";
        }
        return jsonb.toJson(recordedMatch);
    }

    private void recordMatchState(MatchServer server) {
        RecordedMatch recordedMatch = recordedMatchDao.find(server.getId());
        if (recordedMatch == null) {
            recordedMatch = new RecordedMatch();
            recordedMatch.setId(server.getId());
            recordedMatch.setName(server.getConfiguration().getName());
            recordedMatch.addSnapshot(server.getAllClientStates());
            recordedMatchDao.save(recordedMatch);
        } else {
            recordedMatch.addSnapshot(server.getAllClientStates());
            recordedMatchDao.update(recordedMatch);
        }
    }

    private void checkOutcomeForPlayers(MatchServer server) {
        List<Player> players = PlayerService.getPlayersInMatch(server.getId());
        for (Player player : players) {
            checkOutcome(server, player);
        }
    }

    private void checkOutcome(MatchServer server, Player player) {
        MatchOutcome outcome = server.getClientState(player.getHandId()).getOutcome();
        if (outcome == MatchOutcome.Victory) {
            onVictory(player);
        } else if (outcome == MatchOutcome.Defeat) {
            onDefeat(player);
        }
    }

    private void onVictory(Player player) {
        PlayerProfile profile = playerProfileDao.find(player.getId());
        profile.addVictory();
        playerProfileDao.update(profile);
    }

    private void onDefeat(Player player) {
        PlayerProfile profile = playerProfileDao.find(player.getId());
        profile.addDefeat();
        playerProfileDao.update(profile);
    }

}
