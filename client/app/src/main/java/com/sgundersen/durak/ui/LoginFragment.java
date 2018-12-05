package com.sgundersen.durak.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgundersen.durak.R;
import com.sgundersen.durak.net.auth.GoogleLogin;
import com.sgundersen.durak.ui.lobby.LobbyTableFragment;
import com.sgundersen.durak.ui.match.LocalMatchFragment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class LoginFragment extends MainActivityFragment implements View.OnClickListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_login, null);
        view.findViewById(R.id.login_sign_in).setOnClickListener(this);
        view.findViewById(R.id.play_local_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().hideNavigationFragment();
        GoogleLogin.tryLastSignIn(this);
    }

    @Override
    public void onClick(View targetView) {
        if (targetView.getId() == R.id.login_sign_in) {
            GoogleLogin.tryNewSignIn(getMainActivity());
        } else if (targetView.getId() == R.id.play_local_button) {
            getMainActivity().setMainFragment(new LocalMatchFragment());
        }
    }

    public void onLoginSuccess() {
        getMainActivity().setMainFragment(new LobbyTableFragment());
        getMainActivity().showNavigationFragment();
    }

    public void onLoginFailed() {

    }

}
