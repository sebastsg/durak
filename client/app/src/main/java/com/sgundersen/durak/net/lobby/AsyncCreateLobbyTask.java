package com.sgundersen.durak.net.lobby;

import com.sgundersen.durak.net.AsyncHttpTask;
import com.sgundersen.durak.ui.lobby.LobbyTableFragment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncCreateLobbyTask extends AsyncHttpTask<String, Void, Boolean> {

    private final LobbyTableFragment lobbyTableFragment;

    @Override
    protected Boolean doInBackground(String... strings) {
        return gson.fromJson(post("lobby/create"), Boolean.class);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success != null && success) {
            lobbyTableFragment.onLobbyJoined();
        }
    }

}
