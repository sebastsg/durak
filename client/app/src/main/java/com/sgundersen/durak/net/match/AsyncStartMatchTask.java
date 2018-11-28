package com.sgundersen.durak.net.match;

import com.sgundersen.durak.net.AsyncHttpTask;
import com.sgundersen.durak.ui.lobby.LobbyFragment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncStartMatchTask extends AsyncHttpTask<String, Void, Boolean> {

    private final LobbyFragment lobbyFragment;

    @Override
    protected Boolean doInBackground(String... strings) {
        return gson.fromJson(post("lobby/start"), Boolean.class);
    }

    @Override
    protected void onPostExecute(Boolean data) {
        if (data != null && data) {
            lobbyFragment.onMatchStarted();
        }
    }

}
