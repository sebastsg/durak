package com.sgundersen.durak.net.lobby;

import com.sgundersen.durak.ui.lobby.LobbyTable;

import java.util.TimerTask;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncGetLobbiesTimerTask extends TimerTask {

    private final LobbyTable lobbyTable;

    @Override
    public void run() {
        new AsyncGetLobbiesTask(lobbyTable).execute();
    }

}
