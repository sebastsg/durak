package com.sgundersen.durak.server.service;

import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.net.lobby.LobbyItemInfo;
import com.sgundersen.durak.core.net.lobby.LobbyItemInfoList;
import com.sgundersen.durak.core.net.lobby.LobbyState;
import com.sgundersen.durak.server.db.MatchDao;
import com.sgundersen.durak.server.db.MatchEntity;
import com.sgundersen.durak.server.db.PlayerEntity;
import com.sgundersen.durak.server.lobby.Lobby;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("lobby")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class LobbyService {

    private static final Jsonb jsonb = JsonbBuilder.create();
    private static final String JSON_TRUE = jsonb.toJson(true);
    private static final String JSON_FALSE = jsonb.toJson(false);

    private static final Map<Long, Lobby> lobbies = new ConcurrentHashMap<>();

    private static PlayerEntity getPlayer(HttpServletRequest request) {
        return request == null ? null : PlayerService.getPlayer(request.getSession().getId());
    }

    private static Lobby getLobby(PlayerEntity player) {
        return player == null ? null : lobbies.get(player.getMatchId());
    }

    @Inject
    private MatchDao matchDao;

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
        PlayerEntity player = getPlayer(request);
        if (player == null) {
            return JSON_FALSE;
        }
        MatchEntity match = new MatchEntity();
        match.setName(player.getDisplayName() + "'s match");
        matchDao.save(match);
        Lobby lobby = new Lobby(match.getId(), match.getName());
        lobbies.put(match.getId(), lobby);
        lobby.addPlayer(player);
        return JSON_TRUE;
    }

    @POST
    @Path("join")
    @Consumes(MediaType.APPLICATION_JSON)
    public String join(@Context HttpServletRequest request, String json) {
        long matchId = jsonb.fromJson(json, Long.class);
        if (!lobbies.containsKey(matchId)) {
            return JSON_FALSE;
        }
        Lobby lobby = lobbies.get(matchId);
        if (lobby == null || !lobby.isAcceptingPlayers()) {
            return JSON_FALSE;
        }
        lobby.addPlayer(getPlayer(request));
        return JSON_TRUE;
    }

    @GET
    @Path("view")
    public String view(@Context HttpServletRequest request) {
        PlayerEntity player = getPlayer(request);
        Lobby lobby = getLobby(player);
        LobbyState state = new LobbyState();
        if (lobby == null) {
            MatchServer server = MatchService.getServer(player);
            if (server == null) {
                return "";
            }
            state.setStarted(true);
        } else {
            for (PlayerEntity playerInMatch : PlayerService.getPlayersInMatch(lobby.getId())) {
                state.getPlayers().add(playerInMatch.getDisplayName());
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
