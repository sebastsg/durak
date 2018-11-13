package com.sgundersen.durak.net;

import com.google.gson.JsonSyntaxException;
import com.sgundersen.durak.core.net.MatchLobbyInfoList;
import com.sgundersen.durak.ui.MatchesView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncGetMatchListTask extends AsyncHttpTask<String, Void, MatchLobbyInfoList> {

    private final MatchesView matchesView;

    @Override
    protected MatchLobbyInfoList doInBackground(String... parameters) {
        try {
            return gson.fromJson(get("match/list"), MatchLobbyInfoList.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(MatchLobbyInfoList data) {
        if (data == null) {
            log.error("Data is null!");
            return;
        }
        matchesView.setMatchList(data);
    }
}
