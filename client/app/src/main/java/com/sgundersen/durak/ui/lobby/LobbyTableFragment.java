package com.sgundersen.durak.ui.lobby;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sgundersen.durak.R;
import com.sgundersen.durak.net.lobby.AsyncCreateLobbyTask;
import com.sgundersen.durak.net.lobby.AsyncGetLobbiesTimerTask;
import com.sgundersen.durak.ui.MainActivityFragment;

import java.util.Timer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LobbyTableFragment extends MainActivityFragment implements View.OnClickListener {

    private Timer refreshTimer = new Timer();
    private LobbyTable lobbyTable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lobby_table_view, null);
        lobbyTable = new LobbyTable(view, this);
        Button button = view.findViewById(R.id.create_lobby_button);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().showNavigationFragment();
        refreshTimer.scheduleAtFixedRate(new AsyncGetLobbiesTimerTask(lobbyTable), 0, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshTimer.cancel();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.create_lobby_button) {
            new AsyncCreateLobbyTask(this).execute();
        }
    }

    public void onLobbyJoined() {
        getMainActivity().setMainFragment(new LobbyFragment());
    }

}
