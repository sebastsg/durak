package com.sgundersen.durak.match;

import com.sgundersen.durak.core.match.MatchOutcome;
import com.sgundersen.durak.core.net.MatchClientState;
import com.sgundersen.durak.core.net.PlayerAction;
import com.sgundersen.durak.draw.gl.GLMatchRenderer;
import com.sgundersen.durak.net.AsyncSendActionTask;
import com.sgundersen.durak.net.timer.AsyncUpdateClientStateTimerTask;

import java.util.Timer;

import lombok.Getter;
import lombok.Setter;

public class MatchClient {

    private final GLMatchRenderer renderer;

    @Getter
    private MatchClientState state;

    @Setter
    private MatchClientState nextState;

    private Timer refreshTimer = new Timer();

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
        AsyncSendActionTask sendActionTask = new AsyncSendActionTask(this, PlayerAction.useCard(cardIndex));
        sendActionTask.execute();
    }

    public void takeCards() {
        AsyncSendActionTask sendActionTask = new AsyncSendActionTask(this, PlayerAction.takeCards());
        sendActionTask.execute();
    }

    public void endTurn() {
        AsyncSendActionTask sendActionTask = new AsyncSendActionTask(this, PlayerAction.endTurn());
        sendActionTask.execute();
    }

    public boolean isFinished() {
        return state.getOutcome() != MatchOutcome.NotYetDecided;
    }

}
