package com.sgundersen.durak.control.local;

import com.sgundersen.durak.control.MatchClient;
import com.sgundersen.durak.core.net.match.Action;

import lombok.Setter;

public class LocalMatchClient extends MatchClient {

    @Setter
    private LocalStateController stateController;

    @Override
    protected void onAction(Action action) {
        if (stateController != null) {
            stateController.runAction(action);
        }
    }

}
