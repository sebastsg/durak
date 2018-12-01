package com.sgundersen.durak.draw;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sgundersen.durak.control.MatchClient;
import com.sgundersen.durak.control.MatchController;
import com.sgundersen.durak.control.live.LiveStateController;
import com.sgundersen.durak.draw.gl.GLMatchRenderer;
import com.sgundersen.durak.ui.match.MatchFragment;

import org.joml.Vector2f;

public class MatchSurfaceView extends GLSurfaceView {

    private Vector2f begin = new Vector2f(0.0f);
    private Vector2f end = new Vector2f(0.0f);

    private MatchController matchController;
    private LiveStateController liveStateController;
    private MatchFragment matchFragment;

    public MatchSurfaceView(Context context) {
        super(context);
    }

    public MatchSurfaceView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void initialize(MatchFragment matchFragment) {
        this.matchFragment = matchFragment;
        setEGLContextClientVersion(3);
        liveStateController = new LiveStateController(new MatchClient());
        GLMatchRenderer renderer = new GLMatchRenderer(getContext(), liveStateController);
        matchController = renderer.getMatchController();
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Having to tap lets the player see the outcome before continuing.
                if (liveStateController.getMatchClient().isFinished() && matchFragment != null) {
                    matchFragment.onMatchFinished();
                } else {
                    begin.x = event.getX();
                    begin.y = event.getY();
                    end.x = begin.x;
                    end.y = begin.y;
                    matchController.onTouchDown(begin);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                end.x = event.getX();
                end.y = event.getY();
                matchController.onTouchMove(begin, end, new Vector2f(end.x - begin.x, end.y - begin.y));
                break;
            case MotionEvent.ACTION_UP:
                end.x = event.getX();
                end.y = event.getY();
                matchController.onTouchUp(begin, end, new Vector2f(end.x - begin.x, end.y - begin.y));
                break;
            default:
                break;
        }
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}