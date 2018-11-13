package com.sgundersen.durak.ui;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.sgundersen.durak.MainActivity;
import com.sgundersen.durak.R;
import com.sgundersen.durak.core.net.MatchConfiguration;
import com.sgundersen.durak.core.net.MatchLobbyInfo;
import com.sgundersen.durak.core.net.MatchLobbyInfoList;
import com.sgundersen.durak.net.AsyncCreateMatchTask;
import com.sgundersen.durak.net.AsyncJoinMatchTask;
import com.sgundersen.durak.net.timer.AsyncGetMatchListTimerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MatchesView implements TabView, View.OnClickListener, AdapterView.OnItemClickListener {

    @Getter
    private final MainActivity activity;

    private ListView matchList;
    private Button createButton;

    @Getter
    private MatchLobbyInfoList lobbyList;

    private View view;

    private Timer refreshTimer = new Timer();

    public void show() {
        view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.view_matches,null, false);
        activity.getLayout().addView(view, 0);
        matchList = activity.findViewById(R.id.match_list);
        matchList.setOnItemClickListener(this);
        createButton = activity.findViewById(R.id.create_match_button);
        createButton.setOnClickListener(this);
        refreshTimer.scheduleAtFixedRate(new AsyncGetMatchListTimerTask(this), 0, 5000);
    }

    public void hide() {
        refreshTimer.cancel();
        activity.getLayout().removeView(view);
    }

    public void setMatchList(MatchLobbyInfoList list) {
        if (list == null) {
            log.error("Received null match list");
            return;
        }
        log.info("Refreshing match list");
        lobbyList = list;
        List<String> matches = new ArrayList<>();
        for (MatchLobbyInfo lobby : list.getLobbies()) {
            matches.add(lobby.getName() + " [" + lobby.getCurrentPlayers() + "/" + lobby.getMaxPlayers() + "]");
        }
        ArrayAdapter<String> matchAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, matches);
        matchList.setAdapter(matchAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == createButton.getId()) {
            MatchConfiguration configuration = new MatchConfiguration(6, 2);
            AsyncCreateMatchTask createMatchTask = new AsyncCreateMatchTask(this, configuration);
            createMatchTask.execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MatchLobbyInfo lobbyInfo = getLobbyList().getLobbies().get(position);
        AsyncJoinMatchTask joinMatchTask = new AsyncJoinMatchTask(lobbyInfo, this);
        joinMatchTask.execute();
    }

}
