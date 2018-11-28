package com.sgundersen.durak.net.lobby;

import com.sgundersen.durak.net.AsyncHttpTask;
import com.sgundersen.durak.ui.lobby.LobbyTableFragment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncJoinLobbyTask extends AsyncHttpTask<String, Void, Boolean> {

    private final int lobbyId;
    private final LobbyTableFragment lobbyTableFragment;

    @Override
    protected Boolean doInBackground(String... strings) {
        return gson.fromJson(post("lobby/join", gson.toJson(lobbyId)), Boolean.class);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success != null && success) {
            lobbyTableFragment.onLobbyJoined();
        }
    }

}
