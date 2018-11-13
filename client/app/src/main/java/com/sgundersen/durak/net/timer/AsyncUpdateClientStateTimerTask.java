package com.sgundersen.durak.net.timer;

import com.sgundersen.durak.match.MatchClient;
import com.sgundersen.durak.net.AsyncUpdateClientStateTask;

import java.util.TimerTask;

public class AsyncUpdateClientStateTimerTask extends TimerTask {

    private MatchClient matchClient;

    public AsyncUpdateClientStateTimerTask(MatchClient matchClient) {
        this.matchClient = matchClient;
    }

    @Override
    public void run() {
        if (matchClient.isUpdated()) {
            AsyncUpdateClientStateTask task = new AsyncUpdateClientStateTask(matchClient);
            task.execute();
        }
    }

}
