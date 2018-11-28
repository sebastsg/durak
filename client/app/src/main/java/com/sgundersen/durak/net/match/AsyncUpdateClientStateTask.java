package com.sgundersen.durak.net.match;

import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.match.MatchClient;
import com.sgundersen.durak.net.AsyncHttpTask;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncUpdateClientStateTask extends AsyncHttpTask<String, Void, MatchClientState> {

    private final MatchClient matchClient;

    @Override
    protected MatchClientState doInBackground(String... strings) {
        return gson.fromJson(get("match/state"), MatchClientState.class);
    }

    @Override
    protected void onPostExecute(MatchClientState state) {
        if (state != null) {
            matchClient.setNextState(state);
        }
    }

}
