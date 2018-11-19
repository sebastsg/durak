package com.sgundersen.durak.server;

import com.sgundersen.durak.core.net.LoginAttempt;
import com.sgundersen.durak.server.auth.GoogleAuthResponse;

import javax.ejb.Stateless;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

@Path("player")
@Produces(MediaType.TEXT_PLAIN)
@Stateless
public class PlayerService {

    // TODO: Not store this here.
    private static final String CLIENT_ID = "317207620584-26b2id4cr2i4shgan1r6vppk8if5l8h8.apps.googleusercontent.com";

    private static final Jsonb jsonb = JsonbBuilder.create();
    private static final Map<String, Player> players = new ConcurrentHashMap<>();

    public static Player getPlayer(String sessionId) {
        return players.get(sessionId);
    }

    public static List<Player> getPlayersInMatch(int matchId) {
        List<Player> playersInMatch = new ArrayList<>();
        for (Player player : players.values()) {
            if (player.getActiveMatchId().get() == matchId) {
                playersInMatch.add(player);
            }
        }
        return playersInMatch;
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

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(@Context HttpServletRequest request, String json) {
        LoginAttempt loginAttempt = jsonb.fromJson(json, LoginAttempt.class);
        if (loginAttempt == null) {
            return jsonb.toJson(false, Boolean.class);
        }
        /*GoogleAuthResponse authResponse = validateTokenId(loginAttempt.getCurrentTokenId());
        if (authResponse == null) {
            System.err.println("Error validating the specified token id");
            return "";
        }
        boolean isValid = authResponse.getAud().equals(CLIENT_ID);
        if (!isValid) {
            System.err.println("The login has not been registered with this application yet.");
            return "";
        }
        String accountId = authResponse.getSub();*/
        String accountId = loginAttempt.getAccountId();
        HttpSession session = request.getSession();
        System.out.println("Player " + loginAttempt.getName() + " (" + loginAttempt.getEmail() + ") [" + accountId + "] has logged in. Session: " + session.getId());
        players.put(session.getId(), new Player(loginAttempt.getName(), loginAttempt.getEmail(), accountId));
        return jsonb.toJson(true, Boolean.class);
    }

}
