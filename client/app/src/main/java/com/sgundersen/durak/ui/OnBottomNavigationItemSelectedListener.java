package com.sgundersen.durak.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.sgundersen.durak.MainActivity;
import com.sgundersen.durak.R;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnBottomNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final MainActivity activity;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_matches:
                return activity.setTabView(new MatchesView(activity));
            case R.id.navigation_leaderboard:
                return activity.setTabView(new LeaderboardView(activity));
            case R.id.navigation_rules:
                return activity.setTabView(new RulesView(activity));
            default:
                return false;
        }
    }

}
