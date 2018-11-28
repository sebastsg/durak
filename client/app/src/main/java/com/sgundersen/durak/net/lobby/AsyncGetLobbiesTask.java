package com.sgundersen.durak.net.lobby;

import com.google.gson.JsonSyntaxException;
import com.sgundersen.durak.core.net.lobby.LobbyItemInfoList;
import com.sgundersen.durak.net.AsyncHttpTask;
import com.sgundersen.durak.ui.lobby.LobbyTable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncGetLobbiesTask extends AsyncHttpTask<String, Void, LobbyItemInfoList> {

    private final LobbyTable lobbyTableView;

    @Override
    protected LobbyItemInfoList doInBackground(String... parameters) {
        try {
            return gson.fromJson(get("lobby/list"), LobbyItemInfoList.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(LobbyItemInfoList data) {
        if (data != null) {
            lobbyTableView.setMatchList(data);
        }
    }

}
