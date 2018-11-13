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

    @GET
    @Path("list")
    public String list(@Context HttpServletRequest request, String json) {
        MatchLobbyInfoList matches = new MatchLobbyInfoList();
        for (MatchServer server : servers.values()) {
            if (!server.isAcceptingPlayers()) {
                continue;
            }
            matches.getLobbies().add(new MatchLobbyInfo(server.getId(), "Match #" + server.getId(), server.getConfiguration().getMaxPlayers(), server.getPlayerCount()));
        }
        return jsonb.toJson(matches);
    }

    @POST
    @Path("create")
    public String create(@Context HttpServletRequest request, String json) {
        MatchConfiguration configuration = jsonb.fromJson(json, MatchConfiguration.class);
        if (configuration == null) {
            System.err.println("Invalid request");
            return "";
        }
        if (!configuration.isValid()) {
            return "";
        }
        int newId = serverIdCounter.incrementAndGet();
        MatchServer server = new MatchServer(newId, configuration);
        servers.put(newId, server);
        int playerId = server.addPlayer();
        if (playerId == -1) {
            System.err.println("Unable to add player to match");
            return "";
        }
        Player player = PlayerService.getPlayer(request.getSession().getId());
        if (player == null) {
            System.err.println("Player not found. Invalid session maybe?");
            return "";
        }
        player.getActiveMatchId().set(newId);
        player.getActiveMatchPlayerId().set(playerId);
        MatchLobbyInfo info = new MatchLobbyInfo(newId, player.getName() + "'s lobby", configuration.getMaxPlayers(), server.getPlayerCount());
        return jsonb.toJson(info);
    }

    @POST
    @Path("join")
    public String join(@Context HttpServletRequest request, String json) {
        int matchId = jsonb.fromJson(json, Integer.class);
        if (!servers.containsKey(matchId)) {
            return jsonb.toJson(false);
        }
        Player player = PlayerService.getPlayer(request.getSession().getId());
        if (player == null) {
            System.err.println("Player not found. Invalid session: " + request.getSession().getId());
            return jsonb.toJson(false);
        }
        MatchServer server = servers.get(matchId);
        if (!server.isAcceptingPlayers()) {
            return jsonb.toJson(false);
        }
        int playerId = server.addPlayer();
        if (playerId == -1) {
            return jsonb.toJson(false);
        }
        player.getActiveMatchId().set(matchId);
        player.getActiveMatchPlayerId().set(playerId);
        return jsonb.toJson(true);
    }

    @GET
    @Path("lobby")
    public String lobby(@Context HttpServletRequest request, String json) {
        Player player = PlayerService.getPlayer(request.getSession().getId());
        if (player == null) {
            System.err.println("Player not found. Invalid session maybe?");
            return "";
        }
        int matchId = player.getActiveMatchId().get();
        MatchServer server = servers.get(matchId);
        MatchLobbyDetails lobbyDetails = new MatchLobbyDetails();
        for (Player playerInMatch : PlayerService.getPlayersInMatch(matchId)) {
            lobbyDetails.getPlayers().add(playerInMatch.getName());
        }
        lobbyDetails.setStarted(server.isStarted());
        return jsonb.toJson(lobbyDetails);
    }

    @POST
    @Path("start")
    public String start(@Context HttpServletRequest request, String json) {
        Player player = PlayerService.getPlayer(request.getSession().getId());
        if (player == null) {
            System.err.println("Player not found. Invalid session maybe?");
            return jsonb.toJson(false);
        }
        int matchId = player.getActiveMatchId().get();
        MatchServer server = servers.get(matchId);
        if (server.isStarted()) {
            System.err.println("Cannot start an already started match");
            return jsonb.toJson(false);
        }
        if (2 > server.getPlayerCount()) {
            System.err.println("Cannot start this match yet");
            return jsonb.toJson(false);
        }
        server.start();
        return jsonb.toJson(true);
    }

    @GET
    @Path("state")
    public String state(@Context HttpServletRequest request) {
        if (request == null) {
            System.err.println("Invalid request");
            return "";
        }
        Player player = PlayerService.getPlayer(request.getSession().getId());
        if (player == null) {
            System.err.println("Invalid session " + request.getSession().getId() + " at " + request.getRequestURI());
            return "";
        }
        MatchServer server = servers.get(player.getActiveMatchId().get());
        if (server == null) {
            System.err.println("Player is not in a match");
            return "";
        }
        if (!server.isStarted()) {
            System.err.println("The match has not started yet.");
            return "";
        }
        int playerId = player.getActiveMatchPlayerId().get();
        MatchClientState state = server.getClientState(playerId);
        if (state == null) {
            System.err.println("There is no state for player " + playerId);
            return "";
        }
        return jsonb.toJson(state);
    }

    @POST
    @Path("action")
    public String playerAction(@Context HttpServletRequest request, String json) {
        if (request == null) {
            System.err.println("Invalid request");
            return "";
        }
        Player player = PlayerService.getPlayer(request.getSession().getId());
        if (player == null) {
            System.err.println("Invalid session " + request.getSession().getId() + " at " + request.getRequestURI());
            return "";
        }
        MatchServer server = servers.get(player.getActiveMatchId().get());
        if (server == null) {
            System.err.println("Player is not in a match");
            return "";
        }
        PlayerAction action = jsonb.fromJson(json, PlayerAction.class);
        int playerId = player.getActiveMatchPlayerId().get();
        server.processAction(playerId, action);
        return jsonb.toJson(server.getClientState(playerId));
    }

}
