package com.sgundersen.durak.net.lobby;

import com.sgundersen.durak.ui.lobby.LobbyFragment;

import java.util.TimerTask;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncViewLobbyTimerTask extends TimerTask {

    private final LobbyFragment lobbyFragment;

    @Override
    public void run() {
        new AsyncViewLobbyTask(lobbyFragment).execute();
    }

}
