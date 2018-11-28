package com.sgundersen.durak.server.service;

import com.sgundersen.durak.core.match.MatchConfiguration;
import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.net.lobby.LobbyItemInfo;
import com.sgundersen.durak.core.net.lobby.LobbyItemInfoList;
import com.sgundersen.durak.core.net.lobby.LobbyState;
import com.sgundersen.durak.server.lobby.Lobby;
import com.sgundersen.durak.server.match.Player;

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

@Path("lobby")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class LobbyService {

    private static final Jsonb jsonb = JsonbBuilder.create();
    private static final String JSON_TRUE = jsonb.toJson(true);
    private static final String JSON_FALSE = jsonb.toJson(false);

    private static final Map<Integer, Lobby> lobbies = new ConcurrentHashMap<>();
    private static AtomicInteger lobbyIdCounter = new AtomicInteger(0);

    private static Player getPlayer(HttpServletRequest request) {
        return request == null ? null : PlayerService.getPlayer(request.getSession().getId());
    }

    private static Lobby getLobby(Player player) {
        return player == null ? null : lobbies.get(player.getMatchId());
    }

    @GET
    @Path("list")
    public String list(@Context HttpServletRequest request) {
        LobbyItemInfoList items = new LobbyItemInfoList();
        for (Lobby lobby : lobbies.values()) {
            items.add(new LobbyItemInfo(lobby.getId(), lobby.getPlayerCount(), lobby.getConfiguration()));
        }
        return jsonb.toJson(items);
    }

    @POST
    @Path("create")
    public String create(@Context HttpServletRequest request) {
        int newLobbyId = lobbyIdCounter.incrementAndGet();
        Lobby lobby = new Lobby(newLobbyId, new MatchConfiguration());
        lobbies.put(newLobbyId, lobby);
        Player player = getPlayer(request);
        int playerId = lobby.addPlayer(player);
        if (playerId == -1) {
            return JSON_FALSE;
        }
        player.set(newLobbyId, playerId);
        return JSON_TRUE;
    }

    @POST
    @Path("join")
    @Consumes(MediaType.APPLICATION_JSON)
    public String join(@Context HttpServletRequest request, String json) {
        int matchId = jsonb.fromJson(json, Integer.class);
        if (!lobbies.containsKey(matchId)) {
            return JSON_FALSE;
        }
        Player player = getPlayer(request);
        Lobby lobby = lobbies.get(matchId);
        if (lobby == null || !lobby.isAcceptingPlayers()) {
            return JSON_FALSE;
        }
        int handId = lobby.addPlayer(player);
        player.set(matchId, handId);
        return JSON_TRUE;
    }

    @GET
    @Path("view")
    public String view(@Context HttpServletRequest request) {
        Player player = getPlayer(request);
        Lobby lobby = getLobby(player);
        LobbyState state = new LobbyState();
        if (lobby == null) {
            MatchServer server = MatchService.getServer(player);
            if (server == null) {
                return "";
            }
            state.setStarted(true);
        } else {
            for (Player playerInMatch : PlayerService.getPlayersInMatch(lobby.getId())) {
                state.getPlayers().add(playerInMatch.getName());
            }
            state.setInfo(new LobbyItemInfo(lobby.getId(), lobby.getPlayerCount(), lobby.getConfiguration()));
        }
        return jsonb.toJson(state);
    }

    @POST
    @Path("start")
    public String start(@Context HttpServletRequest request) {
        Lobby lobby = getLobby(getPlayer(request));
        if (!MatchService.start(lobby)) {
            return JSON_FALSE;
        }
        lobbies.remove(lobby.getId());
        return JSON_TRUE;
    }

}
