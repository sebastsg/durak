package com.sgundersen.durak.draw;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sgundersen.durak.control.PlaybackStateController;
import com.sgundersen.durak.draw.gl.GLMatchRenderer;
import com.sgundersen.durak.control.MatchClient;
import com.sgundersen.durak.ui.match.MatchPlaybackFragment;

public class RecordedMatchSurfaceView extends GLSurfaceView {

    private MatchPlaybackFragment matchPlaybackFragment;
    private PlaybackStateController playbackStateController;

    public RecordedMatchSurfaceView(Context context) {
        super(context);
    }

    public RecordedMatchSurfaceView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void initialize(MatchPlaybackFragment matchPlaybackFragment) {
        this.matchPlaybackFragment = matchPlaybackFragment;
        setEGLContextClientVersion(3);
        long matchId = matchPlaybackFragment.getMatchId();
        playbackStateController = new PlaybackStateController(new MatchClient(), matchId);
        setRenderer(new GLMatchRenderer(getContext(), playbackStateController));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (playbackStateController.isFinished()) {
                matchPlaybackFragment.onPlaybackFinished();
            } else {
                playbackStateController.next();
            }
        }
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}