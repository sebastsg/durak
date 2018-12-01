package com.sgundersen.durak.net.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.sgundersen.durak.core.net.player.LoginAttempt;
import com.sgundersen.durak.net.AsyncHttpTask;
import com.sgundersen.durak.ui.LoginFragment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncLoginTask extends AsyncHttpTask<String, Void, Boolean> {

    private final LoginFragment loginFragment;
    private final LoginAttempt loginAttempt;

    public AsyncLoginTask(LoginFragment loginFragment, GoogleSignInAccount account) {
        this.loginFragment = loginFragment;
        loginAttempt = new LoginAttempt(account.getDisplayName(), account.getId(), account.getIdToken());
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return gson.fromJson(post("player/login", gson.toJson(loginAttempt)), Boolean.class);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success != null && success) {
            loginFragment.onLoginSuccess();
        } else {
            loginFragment.onLoginFailed();
        }
    }

}
