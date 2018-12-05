package com.sgundersen.durak.control.live;

import com.sgundersen.durak.control.MatchClient;
import com.sgundersen.durak.core.net.match.Action;
import com.sgundersen.durak.net.match.AsyncSendActionTask;

public class LiveMatchClient extends MatchClient {

    @Override
    protected void onAction(Action action) {
        new AsyncSendActionTask(this, action).execute();
    }

}
