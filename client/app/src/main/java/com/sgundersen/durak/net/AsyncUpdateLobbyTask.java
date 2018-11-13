package com.sgundersen.durak.net;

import com.sgundersen.durak.core.net.MatchLobbyDetails;
import com.sgundersen.durak.ui.LobbyView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncUpdateLobbyTask extends AsyncHttpTask<String, Void, MatchLobbyDetails> {

    private final LobbyView lobbyView;

    @Override
    protected MatchLobbyDetails doInBackground(String... strings) {
        return gson.fromJson(get("match/lobby"), MatchLobbyDetails.class);
    }

    @Override
    protected void onPostExecute(MatchLobbyDetails data) {
        if (data == null) {
            log.warn("Failed to update lobby.");
            return;
        }
        lobbyView.update(data);
    }

}
