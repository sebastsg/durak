package com.sgundersen.durak.control;

import com.sgundersen.durak.core.net.match.FinishedMatch;
import com.sgundersen.durak.net.match.AsyncGetFinishedMatchTask;

import lombok.Getter;
import lombok.Setter;

public class PlaybackStateController extends StateController {

    @Setter
    private FinishedMatch finishedMatch;

    @Getter
    private boolean finished = false;

    private int currentSnapshot = -1;

    public PlaybackStateController(MatchClient matchClient, long matchId) {
        super(matchClient);
        new AsyncGetFinishedMatchTask(this, matchId).execute();
    }

    @Override
    public void update() {
        super.update();
        if (isReady()) {
            next();
        }
    }

    public void next() {
        if (isWaiting()) {
            return;
        }
        currentSnapshot++;
        finished = currentSnapshot >= finishedMatch.getTotalSnapshots();
        if (!finished) {
            onStateReceived();
            onNextState(finishedMatch.getSnapshot(currentSnapshot).getClientState(0));
        }
    }

}
