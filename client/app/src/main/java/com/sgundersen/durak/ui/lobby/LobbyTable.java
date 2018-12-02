package com.sgundersen.durak.ui.lobby;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sgundersen.durak.R;
import com.sgundersen.durak.core.net.lobby.LobbyItemInfo;
import com.sgundersen.durak.core.net.lobby.LobbyItemInfoList;
import com.sgundersen.durak.net.lobby.AsyncJoinLobbyTask;
import com.sgundersen.durak.ui.TableRowBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LobbyTable implements TableRow.OnClickListener {

    private final Context context;
    private final TableLayout table;
    private final LobbyTableFragment lobbyTableFragment;

    public LobbyTable(View view, LobbyTableFragment lobbyTableFragment) {
        this.lobbyTableFragment = lobbyTableFragment;
        context = view.getContext();
        table = view.findViewById(R.id.match_list);
    }

    private void addHeaderRow() {
        table.addView(new TableRowBuilder(context, R.layout.match_row)
                .set(R.id.host, R.string.name)
                .set(R.id.current_players, R.string.players)
                .set(R.id.max_players, R.string.max)
                .bold()
                .backgroundColor(0xFFEEEEEE)
                .build()
        );
    }

    private void addItemRow(LobbyItemInfo item) {
        table.addView(new TableRowBuilder(context, R.layout.match_row)
                .set(R.id.host, item.getName())
                .set(R.id.current_players, String.valueOf(item.getCurrentPlayers()))
                .set(R.id.max_players, String.valueOf(item.getMaxPlayers()))
                .tag(item.getId())
                .click(this)
                .build()
        );
    }

    public void setMatchList(LobbyItemInfoList list) {
        table.removeAllViews();
        addHeaderRow();
        for (LobbyItemInfo lobby : list.getLobbies()) {
            addItemRow(lobby);
        }
    }

    @Override
    public void onClick(View view) {
        new AsyncJoinLobbyTask((long) view.getTag(), lobbyTableFragment).execute();
    }

}
