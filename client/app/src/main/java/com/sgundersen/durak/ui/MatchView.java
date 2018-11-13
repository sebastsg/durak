package com.sgundersen.durak.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.sgundersen.durak.MainActivity;
import com.sgundersen.durak.R;
import com.sgundersen.durak.draw.MatchSurfaceView;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchView {

    @Getter
    private final MainActivity activity;

    private View view;

    public void show() {
        activity.setTabView(null);
        activity.hideBottomNavigation();
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        view = inflater.inflate(R.layout.view_match,null, false);
        activity.getLayout().addView(view);
        MatchSurfaceView surfaceView = view.findViewById(R.id.match_surface);
        surfaceView.setMatchView(this);
    }

    public void hide() {
        MatchSurfaceView surfaceView = view.findViewById(R.id.match_surface);
        surfaceView.stop();
        activity.getLayout().removeView(view);
        view = null;
    }

    public void onMatchFinished() {
        hide();
        activity.setTabView(new LeaderboardView(activity));
    }

}
