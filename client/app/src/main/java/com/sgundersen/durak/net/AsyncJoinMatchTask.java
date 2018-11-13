package com.sgundersen.durak.net;

import com.sgundersen.durak.core.net.MatchLobbyInfo;
import com.sgundersen.durak.ui.LobbyView;
import com.sgundersen.durak.ui.MatchesView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncJoinMatchTask extends AsyncHttpTask<String, Void, Boolean> {

    private final MatchLobbyInfo lobbyInfo;
    private final MatchesView matchesView;

    @Override
    protected Boolean doInBackground(String... strings) {
        return gson.fromJson(post("match/join", gson.toJson(lobbyInfo.getId())), Boolean.class);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success == null || !success) {
            log.warn("Could not join match.");
            return;
        }
        matchesView.getActivity().setTabView(null);
        LobbyView lobbyView = new LobbyView(matchesView.getActivity(), lobbyInfo);
        lobbyView.show();
    }

}
