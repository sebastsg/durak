package com.sgundersen.durak.ui.match;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgundersen.durak.R;
import com.sgundersen.durak.draw.MatchSurfaceView;
import com.sgundersen.durak.ui.MainActivityFragment;
import com.sgundersen.durak.ui.lobby.LobbyTableFragment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchFragment extends MainActivityFragment {

    private MatchSurfaceView surfaceView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_match, null);
        surfaceView = view.findViewById(R.id.match_surface);
        surfaceView.setMatchFragment(this);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        surfaceView.stop();
    }

    public void onMatchFinished() {
        getMainActivity().setMainFragment(new LobbyTableFragment());
    }

}
