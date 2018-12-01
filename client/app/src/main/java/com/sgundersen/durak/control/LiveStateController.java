package com.sgundersen.durak.control;

import com.sgundersen.durak.net.match.AsyncUpdateClientStateTask;

public class LiveStateController extends StateController {

    public LiveStateController(MatchClient matchClient) {
        super(matchClient);
    }

    @Override
    public void update() {
        super.update();
        if (isReady()) {
            new AsyncUpdateClientStateTask(this).execute();
        }
    }

}
