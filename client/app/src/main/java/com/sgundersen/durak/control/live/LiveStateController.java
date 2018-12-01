package com.sgundersen.durak.control.live;

import com.sgundersen.durak.control.MatchClient;
import com.sgundersen.durak.control.StateController;
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
