package com.sgundersen.durak.control.playback;

import com.sgundersen.durak.control.StateController;
import com.sgundersen.durak.core.net.match.FinishedMatch;
import com.sgundersen.durak.net.match.AsyncGetFinishedMatchTask;
import com.sgundersen.durak.ui.match.MatchFragment;

import lombok.Setter;

public class PlaybackStateController extends StateController {

    @Setter
    private FinishedMatch finishedMatch;

    private int currentSnapshot = -1;

    public PlaybackStateController(long matchId) {
        super(new PlaybackMatchClient());
        new AsyncGetFinishedMatchTask(this, matchId).execute();
    }

    @Override
    public void update() {
        super.update();
        if (isReady()) {
            next();
        }
    }

    @Override
    public boolean isFinished() {
        return currentSnapshot >= finishedMatch.getTotalSnapshots();
    }

    public void next() {
        if (isWaiting()) {
            return;
        }
        currentSnapshot++;
        if (!isFinished()) {
            onStateReceived();
            onNextState(finishedMatch.getSnapshot(currentSnapshot).getClientState(0));
        }
    }

    @Override
    public void onTap(MatchFragment matchFragment) {
        if (isFinished()) {
            matchFragment.onMatchFinished();
        } else {
            next();
        }
    }

}
