package com.sgundersen.durak.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.sgundersen.durak.MainActivity;
import com.sgundersen.durak.R;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RulesView implements TabView {

    private final MainActivity activity;

    public void show() {
        View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.view_rules,null, false);
        activity.getLayout().addView(view, 0);
    }

    public void hide() {
        activity.getLayout().removeViewAt(0);
    }

}
