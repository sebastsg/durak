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
                return getMainActivity().setMainFragmentWithoutBack(new LobbyTableFragment());
            case R.id.navigation_leaderboard:
                return getMainActivity().setMainFragmentWithoutBack(new LeaderboardFragment());
            case R.id.navigation_recordings:
                return getMainActivity().setMainFragmentWithoutBack(new RecordedMatchTableFragment());
            default:
                return false;
        }
    }

}
