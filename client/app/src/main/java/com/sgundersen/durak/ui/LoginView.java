package com.sgundersen.durak.ui;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sgundersen.durak.MainActivity;
import com.sgundersen.durak.R;
import com.sgundersen.durak.core.net.LoginAttempt;
import com.sgundersen.durak.net.AsyncLoginTask;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class LoginView implements View.OnClickListener {

    private static final String CLIENT_ID = "317207620584-26b2id4cr2i4shgan1r6vppk8if5l8h8.apps.googleusercontent.com";
    private static final int RC_SIGN_IN = 9001;

    @Getter
    private final MainActivity activity;

    private View view;

    public void show() {
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        view = inflater.inflate(R.layout.view_login, null, false);
        activity.getLayout().addView(view);
        tryLastSignIn();
    }

    public void hide() {
        activity.getLayout().removeView(view);
    }

    public void onLoginSuccess() {
        hide();
        activity.setTabView(new MatchesView(activity));
    }

    public void onLoginFail() {
        activity.showAlert("The specified login is invalid.");
        TextView message = activity.getLayout().findViewById(R.id.login_message);
        message.setText(R.string.title_login_failed);
    }

    private void tryLastSignIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity.getBaseContext());
        if (account != null) {
            LoginAttempt attempt = new LoginAttempt(account.getDisplayName(), account.getEmail(), account.getId(), account.getIdToken());
            AsyncLoginTask loginTask = new AsyncLoginTask(this, attempt);
            loginTask.execute();
        }
        SignInButton button = activity.getLayout().findViewById(R.id.login_sign_in);
        button.setOnClickListener(this);
    }

    public void onSignInAttemptFinished(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account == null) {
                activity.showAlert("Failed to sign in with the specified Google account.");
                return;
            }
            LoginAttempt attempt = new LoginAttempt(account.getDisplayName(), account.getEmail(), account.getId(), account.getIdToken());
            AsyncLoginTask loginTask = new AsyncLoginTask(this, attempt);
            loginTask.execute();
        } catch (ApiException e) {
            activity.showAlert("An error occurred while signing in: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    private void tryNewSignIn() {
        GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);
        GoogleSignInOptions options = builder.requestEmail().requestIdToken(CLIENT_ID).build();
        GoogleSignInClient client = GoogleSignIn.getClient(activity, options);
        Intent signInIntent = client.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View targetView) {
        if (targetView.getId() == R.id.login_sign_in) {
            tryNewSignIn();
        }
    }

}

