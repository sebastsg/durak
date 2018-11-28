package com.sgundersen.durak.net.match;

import com.sgundersen.durak.match.MatchClient;

import java.util.TimerTask;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncUpdateClientStateTimerTask extends TimerTask {

    private final MatchClient matchClient;

    @Override
    public void run() {
        if (matchClient.isUpdated()) {
            new AsyncUpdateClientStateTask(matchClient).execute();
        }
    }

}
