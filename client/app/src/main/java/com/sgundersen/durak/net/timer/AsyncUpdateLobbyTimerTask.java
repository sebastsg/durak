package com.sgundersen.durak.net.timer;

import com.sgundersen.durak.net.AsyncUpdateLobbyTask;
import com.sgundersen.durak.ui.LobbyView;

import java.util.TimerTask;

public class AsyncUpdateLobbyTimerTask extends TimerTask {

    private LobbyView lobbyView;

    public AsyncUpdateLobbyTimerTask(LobbyView lobbyView) {
        this.lobbyView = lobbyView;
    }

    @Override
    public void run() {
        AsyncUpdateLobbyTask updateLobbyTask = new AsyncUpdateLobbyTask(lobbyView);
        updateLobbyTask.execute();
    }

}
