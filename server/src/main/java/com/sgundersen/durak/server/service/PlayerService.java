package com.sgundersen.durak.server.service;

import com.sgundersen.durak.core.net.player.Leaderboard;
import com.sgundersen.durak.core.net.player.LoginAttempt;
import com.sgundersen.durak.server.auth.GoogleAuthResponse;
import com.sgundersen.durak.server.db.PlayerEntity;
import com.sgundersen.durak.server.db.PlayerDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Path("player")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class PlayerService {

    // TODO: Not store this here.
    private static final String CLIENT_ID = "317207620584-26b2id4cr2i4shgan1r6vppk8if5l8h8.apps.googleusercontent.com";

    private static final Jsonb jsonb = JsonbBuilder.create();
    private static final String JSON_TRUE = jsonb.toJson(true);
    private static final String JSON_FALSE = jsonb.toJson(false);

    private static final Map<String, PlayerEntity> players = new ConcurrentHashMap<>();

    @Inject
    private PlayerDao playerDao;

    public static PlayerEntity getPlayer(String sessionId) {
        return players.get(sessionId);
    }

    public static List<PlayerEntity> getPlayersInMatch(long matchId) {
        List<PlayerEntity> playersInMatch = new ArrayList<>();
        for (PlayerEntity player : players.values()) {
            if (player.getMatchId() == matchId) {
                playersInMatch.add(player);
            }
        }
        return playersInMatch;
    }

    @GET
    @Path("leaderboard")
    public String leaderboard(@Context HttpServletRequest request) {
        List<PlayerEntity> leaders = playerDao.getTop(100);
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setItems(leaders.stream()
                .map(item -> new Leaderboard.Item(item.getDisplayName(), item.getVictories(), item.getDefeats(), item.getRatio()))
                .collect(Collectors.toList()));
        return jsonb.toJson(leaderboard);
    }

    @POST
    @Path("rename")
    @Consumes(MediaType.APPLICATION_JSON)
    public String rename(@Context HttpServletRequest request, String json) {
        String name = jsonb.fromJson(json, String.class);
        if (!isNewNameAllowed(name)) {
            return JSON_FALSE;
        }
        PlayerEntity player = getPlayer(request.getSession().getId());
        if (player == null) {
            return JSON_FALSE;
        }
        player.setDisplayName(name);
        playerDao.save(player);
        return JSON_TRUE;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(@Context HttpServletRequest request, String json) {
        LoginAttempt loginAttempt = jsonb.fromJson(json, LoginAttempt.class);
        if (loginAttempt == null) {
            return JSON_FALSE;
        }
        /*GoogleAuthResponse authResponse = validateTokenId(loginAttempt.getCurrentTokenId());
        if (authResponse == null) {
            System.err.println("Error validating the specified token id");
            return JSON_FALSE;
        }
        boolean isValid = authResponse.getAud().equals(CLIENT_ID);
        if (!isValid) {
            System.err.println("The login has not been registered with this application yet.");
            return JSON_FALSE;
        }
        String accountId = authResponse.getSub();*/
        players.put(request.getSession().getId(), playerDao.createIfNew(loginAttempt));
        return JSON_TRUE;
    }

    private GoogleAuthResponse validateTokenId(String tokenId) {
        String base = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
        String path = base + tokenId;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Connection error. Status code: " + responseCode);
                return null;
            }
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line);
            }
            String json = stringBuilder.toString();
            System.out.println("Received: \n" + json + "\n\n");
            return jsonb.fromJson(stringBuilder.toString(), GoogleAuthResponse.class);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    private static boolean isNewNameAllowed(String name) {
        return name == null || 4 > name.length() || name.length() > 18;
    }

}
