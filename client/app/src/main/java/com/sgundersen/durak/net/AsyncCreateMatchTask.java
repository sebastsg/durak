package com.sgundersen.durak.net;

import com.sgundersen.durak.core.net.MatchConfiguration;
import com.sgundersen.durak.core.net.MatchLobbyInfo;
import com.sgundersen.durak.ui.LobbyView;
import com.sgundersen.durak.ui.MatchesView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncCreateMatchTask extends AsyncHttpTask<String, Void, MatchLobbyInfo> {

    private final MatchesView matchesView;
    private final MatchConfiguration matchConfiguration;

    @Override
    protected MatchLobbyInfo doInBackground(String... strings) {
        return gson.fromJson(post("match/create", gson.toJson(matchConfiguration)), MatchLobbyInfo.class);
    }

    @Override
    protected void onPostExecute(MatchLobbyInfo data) {
        if (data == null) {
            log.error("Response is null!");
            return;
        }
        matchesView.getActivity().setTabView(null);
        LobbyView lobbyView = new LobbyView(matchesView.getActivity(), data);
        lobbyView.show();
    }


}
