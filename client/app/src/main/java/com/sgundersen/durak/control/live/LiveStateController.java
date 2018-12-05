package com.sgundersen.durak.control.live;

import com.sgundersen.durak.control.StateController;
import com.sgundersen.durak.net.match.AsyncUpdateClientStateTask;

public class LiveStateController extends StateController {

    public LiveStateController() {
        super(new LiveMatchClient());
    }

    @Override
    public void update() {
        super.update();
        if (isReady()) {
            new AsyncUpdateClientStateTask(this).execute();
        }
    }

}
