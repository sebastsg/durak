package com.sgundersen.durak.ui.match;

import com.sgundersen.durak.control.StateController;
import com.sgundersen.durak.ui.MainActivityFragment;
import com.sgundersen.durak.ui.lobby.LobbyTableFragment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MatchFragment extends MainActivityFragment {

    @Getter
    private final StateController stateController;

    public void onMatchFinished() {
        getMainActivity().setMainFragment(new LobbyTableFragment());
    }

}
