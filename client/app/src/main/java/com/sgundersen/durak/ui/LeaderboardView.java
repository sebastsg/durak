package com.sgundersen.durak.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.sgundersen.durak.MainActivity;
import com.sgundersen.durak.R;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LeaderboardView implements TabView {

    private final MainActivity activity;

    public void show() {
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        View view = inflater.inflate(R.layout.view_leaderboard,null, false);
        activity.getLayout().addView(view, 0);
    }

    public void hide() {
        activity.getLayout().removeViewAt(0);
    }

}
