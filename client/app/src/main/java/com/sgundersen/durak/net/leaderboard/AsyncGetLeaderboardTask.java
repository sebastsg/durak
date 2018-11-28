package com.sgundersen.durak.net.leaderboard;

import com.google.gson.JsonSyntaxException;
import com.sgundersen.durak.core.net.player.Leaderboard;
import com.sgundersen.durak.net.AsyncHttpTask;
import com.sgundersen.durak.ui.leaderboard.LeaderboardFragment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncGetLeaderboardTask extends AsyncHttpTask<String, Void, Leaderboard> {

    private final LeaderboardFragment leaderboardFragment;

    @Override
    protected Leaderboard doInBackground(String... parameters) {
        try {
            return gson.fromJson(get("player/leaderboard"), Leaderboard.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Leaderboard data) {
        if (data != null) {
            leaderboardFragment.setLeaderboard(data);
        }
    }

}
