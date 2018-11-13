package com.sgundersen.durak.net;

import com.sgundersen.durak.ui.LobbyView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncStartMatchTask extends AsyncHttpTask<String, Void, Boolean> {

    private final LobbyView lobbyView;

    @Override
    protected Boolean doInBackground(String... strings) {
        return gson.fromJson(post("match/start", ""), Boolean.class);
    }

    @Override
    protected void onPostExecute(Boolean data) {
        if (data == null || !data) {
            log.warn("Could not start match.");
            return;
        }
        lobbyView.startMatch();
    }


}
