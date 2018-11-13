package com.sgundersen.durak.net;

import com.sgundersen.durak.core.net.MatchClientState;
import com.sgundersen.durak.match.MatchClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncUpdateClientStateTask extends AsyncHttpTask<String, Void, MatchClientState> {

    private final MatchClient matchClient;

    @Override
    protected MatchClientState doInBackground(String... strings) {
        return gson.fromJson(get("match/state"), MatchClientState.class);
    }

    @Override
    protected void onPostExecute(MatchClientState state) {
        if (state == null) {
            log.warn("Could not get match state.");
            return;
        }
        matchClient.setNextState(state);
    }

}
