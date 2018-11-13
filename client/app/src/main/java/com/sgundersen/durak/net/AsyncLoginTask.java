package com.sgundersen.durak.net;

import android.support.v7.app.AlertDialog;

import com.sgundersen.durak.core.net.LoginAttempt;
import com.sgundersen.durak.ui.LoginView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncLoginTask extends AsyncHttpTask<String, Void, Boolean> {

    private final LoginView loginView;
    private final LoginAttempt loginAttempt;

    @Override
    protected Boolean doInBackground(String... strings) {
        String result = post("player/login", gson.toJson(loginAttempt));
        return gson.fromJson(result, Boolean.class);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result == null) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(loginView.getActivity());
            alertBuilder.setMessage("Login response is null! What the fuck").setTitle("wtf");
            AlertDialog dialog = alertBuilder.create();
            dialog.show();
            log.error("Login response is null!");
            return;
        }
        log.info("Server responded with: {}", result);
        if (result) {
            log.info("Login was successful.");
            loginView.onLoginSuccess();
        } else {
            log.info("Login failed!");
            loginView.onLoginFail();
        }
    }

}
