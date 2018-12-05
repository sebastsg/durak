package com.sgundersen.durak.control.playback;

import com.sgundersen.durak.control.MatchClient;
import com.sgundersen.durak.core.net.match.Action;

public class PlaybackMatchClient extends MatchClient {

    @Override
    protected void onAction(Action action) {

    }

    @Override
    public boolean isInteractive() {
        return false;
    }

}
