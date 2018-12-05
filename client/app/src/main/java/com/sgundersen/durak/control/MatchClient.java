package com.sgundersen.durak.control;

import com.sgundersen.durak.core.match.MatchOutcome;
import com.sgundersen.durak.core.net.match.Action;
import com.sgundersen.durak.core.net.match.MatchClientState;

import lombok.Getter;
import lombok.Setter;

public abstract class MatchClient {

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

    protected abstract void onAction(Action action);

    public void useCard(int cardIndex) {
        onAction(Action.useCard(cardIndex));
    }

    public void takeCards() {
        onAction(Action.takeCards());
    }

    public void endTurn() {
        onAction(Action.endTurn());
    }

    public boolean isFinished() {
        return state.getOutcome() != MatchOutcome.NotYetDecided;
    }

    public boolean isInteractive() {
        return !isFinished() && isInitialized();
    }

}
