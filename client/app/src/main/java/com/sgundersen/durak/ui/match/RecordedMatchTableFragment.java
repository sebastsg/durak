package com.sgundersen.durak.ui.match;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgundersen.durak.R;
import com.sgundersen.durak.net.match.AsyncGetFinishedMatchesTask;
import com.sgundersen.durak.ui.MainActivityFragment;

public class RecordedMatchTableFragment extends MainActivityFragment {

    private RecordedMatchTable recordedMatchTable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recorded_matches_table_view, null);
        recordedMatchTable = new RecordedMatchTable(view, this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().showNavigationFragment();
        new AsyncGetFinishedMatchesTask(recordedMatchTable).execute();
    }

    public void onMatchSelected(long matchId) {
        getMainActivity().setMainFragment(new MatchPlaybackFragment(matchId));
    }

}
