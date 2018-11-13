package com.sgundersen.durak.net.timer;

import com.sgundersen.durak.net.AsyncGetMatchListTask;
import com.sgundersen.durak.ui.MatchesView;

import java.util.TimerTask;

public class AsyncGetMatchListTimerTask extends TimerTask {

    private MatchesView matchesView;

    public AsyncGetMatchListTimerTask(MatchesView matchesView) {
        this.matchesView = matchesView;
    }

    @Override
    public void run() {
        AsyncGetMatchListTask getMatchListTask = new AsyncGetMatchListTask(matchesView);
        getMatchListTask.execute();
    }

}
