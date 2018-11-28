package com.sgundersen.durak.match;

import com.sgundersen.durak.core.match.MatchOutcome;
import com.sgundersen.durak.core.net.match.Action;
import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.draw.gl.GLMatchRenderer;
import com.sgundersen.durak.net.match.AsyncSendActionTask;
import com.sgundersen.durak.net.match.AsyncUpdateClientStateTimerTask;

import java.util.Timer;

import lombok.Getter;
import lombok.Setter;

public class MatchClient {

    private final GLMatchRenderer renderer;
    private final Timer refreshTimer = new Timer();

    @Getter
    private MatchClientState state;

    @Setter
    private MatchClientState nextState;

    public MatchClient(GLMatchRenderer renderer) {
        this.renderer = renderer;
        refreshTimer.scheduleAtFixedRate(new AsyncUpdateClientStateTimerTask(this), 0, 2000);
    }

    public void stop() {
        refreshTimer.cancel();
    }

    public boolean isUpdated() {
        return state == nextState;
    }

    public void update() {
        if (state == null && nextState != null) {
            renderer.setCanInitializeMatch(true);
        }
        state = nextState;
    }

    public void useCard(int cardIndex) {
        new AsyncSendActionTask(this, Action.useCard(cardIndex)).execute();
    }

    public void takeCards() {
        new AsyncSendActionTask(this, Action.takeCards()).execute();
    }

    public void endTurn() {
        new AsyncSendActionTask(this, Action.endTurn()).execute();
    }

    public boolean isFinished() {
        return state.getOutcome() != MatchOutcome.NotYetDecided;
    }

}
