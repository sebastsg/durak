package com.sgundersen.durak.net;

import com.sgundersen.durak.core.net.MatchClientState;
import com.sgundersen.durak.core.net.PlayerAction;
import com.sgundersen.durak.match.MatchClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncSendActionTask extends AsyncHttpTask<String, Void, MatchClientState> {

    private final MatchClient matchClient;
    private final PlayerAction action;

    @Override
    protected MatchClientState doInBackground(String... strings) {
        return gson.fromJson(post("match/action", gson.toJson(action)), MatchClientState.class);
    }

    @Override
    protected void onPostExecute(MatchClientState state) {
        if (state == null) {
            log.error("Failed to send player action");
            return;
        }
        matchClient.setNextState(state);
    }

}
