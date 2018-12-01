package com.sgundersen.durak.net.match;

import com.sgundersen.durak.control.PlaybackStateController;
import com.sgundersen.durak.core.net.match.FinishedMatch;
import com.sgundersen.durak.net.AsyncHttpTask;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncGetFinishedMatchTask extends AsyncHttpTask<String, Void, FinishedMatch> {

    private final PlaybackStateController stateController;
    private final long matchId;

    @Override
    protected void onPreExecute() {
        stateController.onWaitingForState();
    }

    @Override
    protected FinishedMatch doInBackground(String... strings) {
        return gson.fromJson(get("history/match/" + matchId), FinishedMatch.class);
    }

    @Override
    protected void onPostExecute(FinishedMatch finishedMatch) {
        if (finishedMatch != null) {
            stateController.onStateReceived();
            stateController.setFinishedMatch(finishedMatch);
        }
    }

}
