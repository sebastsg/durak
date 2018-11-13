package com.sgundersen.durak.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sgundersen.durak.MainActivity;
import com.sgundersen.durak.R;
import com.sgundersen.durak.core.net.MatchLobbyDetails;
import com.sgundersen.durak.core.net.MatchLobbyInfo;
import com.sgundersen.durak.net.AsyncStartMatchTask;
import com.sgundersen.durak.net.timer.AsyncUpdateLobbyTimerTask;

import java.util.Timer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LobbyView implements View.OnClickListener {

    private final MainActivity activity;
    private final MatchLobbyInfo info;

    private View view;
    private ListView playerListView;
    private Button startButton;

    private Timer refreshTimer = new Timer();

    public void show() {
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        view = inflater.inflate(R.layout.view_lobby, null, false);
        activity.getLayout().addView(view);
        startButton = activity.findViewById(R.id.lobby_start_match);
        startButton.setOnClickListener(this);
        playerListView = activity.findViewById(R.id.lobby_players);
        TextView nameTextView = activity.findViewById(R.id.lobby_name);
        nameTextView.setText(info.getName());
        startUpdate();
    }

    public void hide() {
        refreshTimer.cancel();
        activity.getLayout().removeView(view);
    }

    private void startUpdate() {
        refreshTimer.scheduleAtFixedRate(new AsyncUpdateLobbyTimerTask(this), 0, 5000);
    }

    private boolean canStart() {
        return playerListView.getCount() > 1;
    }

    public void update(MatchLobbyDetails details) {
        ArrayAdapter<String> matchAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, details.getPlayers());
        playerListView.setAdapter(matchAdapter);
        startButton.setClickable(canStart());
        TextView playerCountTextView = activity.findViewById(R.id.lobby_player_count);
        String playerText = details.getPlayers().size() + " / " + info.getMaxPlayers() + " players";
        playerCountTextView.setText(playerText);
        if (details.isStarted()) {
            startMatch();
        }
    }

    public void startMatch() {
        hide();
        MatchView matchView = new MatchView(activity);
        matchView.show();
    }

    @Override
    public void onClick(View targetView) {
        if (targetView.getId() == R.id.lobby_start_match) {
            AsyncStartMatchTask startMatchTask = new AsyncStartMatchTask(this);
            startMatchTask.execute();
        }
    }
}
