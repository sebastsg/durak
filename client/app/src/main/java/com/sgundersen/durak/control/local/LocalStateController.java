package com.sgundersen.durak.control.local;

import com.sgundersen.durak.control.StateController;
import com.sgundersen.durak.core.match.AI;
import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.net.match.Action;

import lombok.Getter;

public class LocalStateController extends StateController {

    @Getter
    private final MatchServer matchServer = new MatchServer();

    public LocalStateController() {
        super(new LocalMatchClient());
        ((LocalMatchClient) getMatchClient()).setStateController(this);
        onNextState(matchServer.getClientState(0));
    }

    public void runAction(Action action) {
        matchServer.onAction(0, action);
        onStateReceived();
        onNextState(matchServer.getClientState(0));
    }

    @Override
    public void update() {
        super.update();
        if (isReady()) {
            runAi();
        }
    }

    private void runAi() {
        for (int playerId = 1; playerId < matchServer.getHandCount(); playerId++) {
            matchServer.onAction(playerId, AI.nextAction(matchServer.getClientState(playerId)));
        }
        onNextState(matchServer.getClientState(0));
        onWaitingForState();
    }

}
