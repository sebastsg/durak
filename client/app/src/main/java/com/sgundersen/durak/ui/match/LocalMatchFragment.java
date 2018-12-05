package com.sgundersen.durak.ui.match;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgundersen.durak.R;
import com.sgundersen.durak.control.local.LocalStateController;
import com.sgundersen.durak.draw.MatchSurfaceView;

public class LocalMatchFragment extends MatchFragment {

    public LocalMatchFragment() {
        super(new LocalStateController());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_match, null);
        MatchSurfaceView surfaceView = view.findViewById(R.id.match_surface);
        surfaceView.initialize(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().hideNavigationFragment();
    }

}
