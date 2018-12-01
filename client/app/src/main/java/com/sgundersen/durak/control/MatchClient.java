package com.sgundersen.durak.control;

import com.sgundersen.durak.core.match.MatchOutcome;
import com.sgundersen.durak.core.net.match.Action;
import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.net.match.AsyncSendActionTask;

import lombok.Getter;
import lombok.Setter;

public class MatchClient {

    @Getter
    private MatchClientState state;

    @Setter
    private MatchClientState nextState;

    @Getter
    private boolean initialized = false;

    public boolean isUpdated() {
        return state == nextState;
    }

    public void update() {
        if (state == null && nextState != null) {
            initialized = true;
        }
        state = nextState;
    }

    private void sendAction(Action action) {
        new AsyncSendActionTask(this, action).execute();
    }

    public void useCard(int cardIndex) {
        sendAction(Action.useCard(cardIndex));
    }

    public void takeCards() {
        sendAction(Action.takeCards());
    }

    public void endTurn() {
        sendAction(Action.endTurn());
    }

    public boolean isFinished() {
        return state.getOutcome() != MatchOutcome.NotYetDecided;
    }

}
