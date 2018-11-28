package com.sgundersen.durak.net.lobby;

import com.sgundersen.durak.core.net.lobby.LobbyState;
import com.sgundersen.durak.net.AsyncHttpTask;
import com.sgundersen.durak.ui.lobby.LobbyFragment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncViewLobbyTask extends AsyncHttpTask<String, Void, LobbyState> {

    private final LobbyFragment lobbyFragment;

    @Override
    protected LobbyState doInBackground(String... strings) {
        return gson.fromJson(get("lobby/view"), LobbyState.class);
    }

    @Override
    protected void onPostExecute(LobbyState state) {
        if (state != null) {
            lobbyFragment.setState(state);
        }
    }

}
