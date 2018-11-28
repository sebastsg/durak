package com.sgundersen.durak.ui.leaderboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.sgundersen.durak.R;
import com.sgundersen.durak.core.net.player.Leaderboard;
import com.sgundersen.durak.net.leaderboard.AsyncGetLeaderboardTask;
import com.sgundersen.durak.ui.MainActivityFragment;
import com.sgundersen.durak.ui.TableRowBuilder;

import java.util.Locale;

public class LeaderboardFragment extends MainActivityFragment {

    private TableLayout table;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        table = (TableLayout) inflater.inflate(R.layout.leaderboard, null);
        return table;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().showNavigationFragment();
        new AsyncGetLeaderboardTask(this).execute();
    }

    private void addHeaderRow() {
        table.addView(new TableRowBuilder(getContext(), R.layout.leaderboard_row)
                .set(R.id.ranking, R.string.rank)
                .set(R.id.name, R.string.name)
                .set(R.id.victories, R.string.victories)
                .set(R.id.defeats, R.string.defeats)
                .set(R.id.ratio, R.string.ratio)
                .bold()
                .backgroundColor(0xFFEEEEEE)
                .build()
        );
    }

    private void addItemRow(int ranking, Leaderboard.Item item) {
        table.addView(new TableRowBuilder(getContext(), R.layout.leaderboard_row)
                .set(R.id.ranking, String.valueOf(ranking))
                .set(R.id.name, item.getPlayerName())
                .set(R.id.victories, String.valueOf(item.getVictories()))
                .set(R.id.defeats, String.valueOf(item.getDefeats()))
                .set(R.id.ratio, String.format(Locale.US, "%.2f", item.getRatio()))
                .build()
        );
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        table.removeAllViews();
        addHeaderRow();
        int ranking = 1;
        for (Leaderboard.Item item : leaderboard.getItems()) {
            addItemRow(ranking, item);
            ranking++;
        }
    }

}
