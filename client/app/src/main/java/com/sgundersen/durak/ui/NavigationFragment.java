package com.sgundersen.durak.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sgundersen.durak.R;
import com.sgundersen.durak.ui.leaderboard.LeaderboardFragment;
import com.sgundersen.durak.ui.lobby.LobbyTableFragment;
import com.sgundersen.durak.ui.match.RecordedMatchTableFragment;
import com.sgundersen.durak.ui.rules.RulesFragment;

public class NavigationFragment extends MainActivityFragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BottomNavigationView view = (BottomNavigationView) inflater.inflate(R.layout.navigation, null);
        view.setOnNavigationItemSelectedListener(this);
        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_matches:
                return getMainActivity().setMainFragment(new LobbyTableFragment());
            case R.id.navigation_leaderboard:
                return getMainActivity().setMainFragment(new LeaderboardFragment());
            case R.id.navigation_rules:
                return getMainActivity().setMainFragment(new RulesFragment());
            case R.id.navigation_recordings:
                return getMainActivity().setMainFragment(new RecordedMatchTableFragment());
            default:
                return false;
        }
    }

}
