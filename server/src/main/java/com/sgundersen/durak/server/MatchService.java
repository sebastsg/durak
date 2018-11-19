package com.sgundersen.durak.server;

import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.net.*;

import javax.ejb.Stateless;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("match")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class MatchService {

    private static final Jsonb jsonb = JsonbBuilder.create();
    private static final Map<Integer, MatchServer> servers = new ConcurrentHashMap<>();
    private static AtomicInteger serverIdCounter = new AtomicInteger(0);

    private static String jsonTrue = jsonb.toJson(true);
    private static String jsonFalse = jsonb.toJson(false);

    private static Player getPlayer(HttpServletRequest request) {
        return request == null ? null : PlayerService.getPlayer(request.getSession().getId());
    }

    private static MatchServer getServer(Player player) {
        return player == null ? null : servers.get(player.getActiveMatchId().get());
    }

    @GET
    @Path("list")
    public String list(@Context HttpServletRequest request, String json) {
        MatchLobbyInfoList matches = new MatchLobbyInfoList();
        for (MatchServer server : servers.values()) {
            if (server.isAcceptingPlayers()) {
                matches.getLobbies().add(new MatchLobbyInfo(server));
            }
        }
        return jsonb.toJson(matches);
    }

    @POST
    @Path("create")
    public String create(@Context HttpServletRequest request, String json) {
        MatchConfiguration configuration = jsonb.fromJson(json, MatchConfiguration.class);
        if (configuration == null || !configuration.isValid()) {
            return "";
        }
        int newMatchId = serverIdCounter.incrementAndGet();
        MatchServer server = new MatchServer(newMatchId, configuration);
        servers.put(newMatchId, server);
        int playerId = server.addPlayer();
        if (playerId == -1) {
            return "";
        }
        Player player = getPlayer(request);
        if (player == null) {
            return "";
        }
        player.setActiveMatch(newMatchId, playerId);
        return jsonb.toJson(new MatchLobbyInfo(server));
    }

    @POST
    @Path("join")
    public String join(@Context HttpServletRequest request, String json) {
        int matchId = jsonb.fromJson(json, Integer.class);
        if (!servers.containsKey(matchId)) {
            return jsonFalse;
        }
        Player player = getPlayer(request);
        MatchServer server = servers.get(matchId);
        if (server == null || !server.isAcceptingPlayers()) {
            return jsonFalse;
        }
        int playerId = server.addPlayer();
        if (playerId == -1) {
            return jsonFalse;
        }
        player.setActiveMatch(matchId, playerId);
        return jsonTrue;
    }

    @GET
    @Path("lobby")
    public String lobby(@Context HttpServletRequest request, String json) {
        Player player = getPlayer(request);
        MatchServer server = getServer(player);
        if (server == null) {
            return "";
        }
        MatchLobbyDetails lobbyDetails = new MatchLobbyDetails();
        for (Player playerInMatch : PlayerService.getPlayersInMatch(player.getActiveMatchId().get())) {
            lobbyDetails.getPlayers().add(playerInMatch.getName());
        }
        lobbyDetails.setStarted(server.isStarted());
        return jsonb.toJson(lobbyDetails);
    }

    @POST
    @Path("start")
    public String start(@Context HttpServletRequest request, String json) {
        MatchServer server = getServer(getPlayer(request));
        if (server == null || server.isStarted() || 2 > server.getPlayerCount()) {
            return jsonFalse;
        }
        server.start();
        return jsonTrue;
    }

    @GET
    @Path("state")
    public String state(@Context HttpServletRequest request) {
        Player player = getPlayer(request);
        MatchServer server = getServer(player);
        if (server == null || !server.isStarted()) {
            return "";
        }
        int playerId = player.getActiveMatchPlayerId().get();
        MatchClientState state = server.getClientState(playerId);
        if (state == null) {
            return "";
        }
        return jsonb.toJson(state);
    }

    @POST
    @Path("action")
    public String action(@Context HttpServletRequest request, String json) {
        Player player = getPlayer(request);
        MatchServer server = getServer(player);
        if (server == null) {
            return "";
        }
        PlayerAction action = jsonb.fromJson(json, PlayerAction.class);
        if (action == null) {
            return "";
        }
        int playerId = player.getActiveMatchPlayerId().get();
        server.processAction(playerId, action);
        return jsonb.toJson(server.getClientState(playerId));
    }

}
