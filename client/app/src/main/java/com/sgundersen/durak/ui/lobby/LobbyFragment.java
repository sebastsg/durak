package com.sgundersen.durak.ui.lobby;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sgundersen.durak.R;
import com.sgundersen.durak.core.net.lobby.LobbyState;
import com.sgundersen.durak.net.match.AsyncStartMatchTask;
import com.sgundersen.durak.net.lobby.AsyncViewLobbyTimerTask;
import com.sgundersen.durak.ui.MainActivityFragment;
import com.sgundersen.durak.ui.match.MatchFragment;

import java.util.Timer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LobbyFragment extends MainActivityFragment implements View.OnClickListener {

    private ListView playerListView;
    private Button startButton;
    private TextView nameTextView;
    private TextView playerCountTextView;
    private Timer refreshTimer = new Timer();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_lobby, null);
        nameTextView = view.findViewById(R.id.lobby_name);
        startButton = view.findViewById(R.id.lobby_start_match);
        playerListView = view.findViewById(R.id.lobby_players);
        playerCountTextView = view.findViewById(R.id.lobby_player_count);
        startButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().hideNavigationFragment();
        refreshTimer.scheduleAtFixedRate(new AsyncViewLobbyTimerTask(this), 0, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshTimer.cancel();
    }

    @Override
    public void onClick(View targetView) {
        if (targetView.getId() == R.id.lobby_start_match ) {
            startMatch();
        }
    }

    public void setState(LobbyState lobby) {
        playerListView.setAdapter(new ArrayAdapter<>(getMainActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, lobby.getPlayers()));
        startButton.setClickable(canStart());
        nameTextView.setText(lobby.getInfo().getName());
        playerCountTextView.setText(lobby.getPlayers().size() + " / " + lobby.getInfo().getMaxPlayers() + " players");
        if (lobby.isStarted()) {
            onMatchStarted();
        }
    }

    private boolean canStart() {
        return playerListView.getCount() > 1;
    }

    private void startMatch() {
        if (canStart()) {
            new AsyncStartMatchTask(this).execute();
        }
    }

    public void onMatchStarted() {
        getMainActivity().setMainFragment(new MatchFragment());
    }

}
