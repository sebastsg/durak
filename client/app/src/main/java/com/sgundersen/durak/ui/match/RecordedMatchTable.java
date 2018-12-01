package com.sgundersen.durak.ui.match;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sgundersen.durak.R;
import com.sgundersen.durak.core.net.match.FinishedMatchItemInfo;
import com.sgundersen.durak.core.net.match.FinishedMatchItemInfoList;
import com.sgundersen.durak.ui.TableRowBuilder;

import java.text.Format;
import java.text.SimpleDateFormat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecordedMatchTable implements TableRow.OnClickListener {

    private final Context context;
    private final TableLayout table;
    private final RecordedMatchTableFragment recordedMatchTableFragment;

    public RecordedMatchTable(View view, RecordedMatchTableFragment recordedMatchTableFragment) {
        this.recordedMatchTableFragment = recordedMatchTableFragment;
        context = view.getContext();
        table = view.findViewById(R.id.recordings_table);
    }

    private void addHeaderRow() {
        table.addView(new TableRowBuilder(context, R.layout.recorded_match_table_row)
                .set(R.id.name, R.string.name)
                .set(R.id.played, R.string.played)
                .bold()
                .backgroundColor(0xFFEEEEEE)
                .build()
        );
    }

    private void addItemRow(FinishedMatchItemInfo item) {
        String playedAgo = DateUtils.getRelativeTimeSpanString(item.getCreatedAt().getTime()).toString();
        table.addView(new TableRowBuilder(context, R.layout.recorded_match_table_row)
                .set(R.id.name, item.getName())
                .set(R.id.played, playedAgo)
                .tag(item.getId())
                .click(this)
                .build()
        );
    }

    public void setFinishedMatchList(FinishedMatchItemInfoList list) {
        table.removeAllViews();
        addHeaderRow();
        for (FinishedMatchItemInfo item : list.getMatches()) {
            addItemRow(item);
        }
    }

    @Override
    public void onClick(View view) {
        recordedMatchTableFragment.onMatchSelected((long) view.getTag());
    }

}
