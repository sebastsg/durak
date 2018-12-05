package com.sgundersen.durak.control.local;

import com.sgundersen.durak.control.MatchClient;
import com.sgundersen.durak.core.match.AI;
import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.net.match.Action;

public class LocalMatchClient extends MatchClient {

    private final MatchServer matchServer = new MatchServer();

    public LocalMatchClient() {
        setNextState(matchServer.getClientState(0));
    }

    @Override
    protected void onAction(Action action) {
        matchServer.onAction(0, action);
        for (int playerId = 1; playerId < matchServer.getHandCount(); playerId++) {
            matchServer.onAction(playerId, AI.nextAction(matchServer.getClientState(playerId)));
        }
        setNextState(matchServer.getClientState(0));
    }

}
