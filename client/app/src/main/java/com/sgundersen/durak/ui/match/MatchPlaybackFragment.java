package com.sgundersen.durak.ui.match;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgundersen.durak.R;
import com.sgundersen.durak.draw.RecordedMatchSurfaceView;
import com.sgundersen.durak.ui.MainActivityFragment;
import com.sgundersen.durak.ui.lobby.LobbyTableFragment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchPlaybackFragment extends MainActivityFragment {

    @Getter
    private final long matchId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recorded_match, null);
        RecordedMatchSurfaceView surfaceView = view.findViewById(R.id.recorded_match_surface);
        surfaceView.initialize(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().hideNavigationFragment();
    }

    public void onPlaybackFinished() {
        getMainActivity().setMainFragment(new LobbyTableFragment());
    }

}
