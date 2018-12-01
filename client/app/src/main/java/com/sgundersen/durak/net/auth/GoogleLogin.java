package com.sgundersen.durak.net.auth;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sgundersen.durak.R;
import com.sgundersen.durak.ui.LoginFragment;

import lombok.extern.slf4j.Slf4j;

import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN;

@Slf4j
public class GoogleLogin {

    public static final int RC_SIGN_IN = 9001;

    public static void tryLastSignIn(LoginFragment loginFragment) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(loginFragment.getMainActivity());
        if (account != null) {
            new AsyncLoginTask(loginFragment, account).execute();
        }
    }

    public static void tryNewSignIn(Activity activity) {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(activity.getString(R.string.client_id))
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(activity, options);
        activity.startActivityForResult(client.getSignInIntent(), RC_SIGN_IN);
    }

    public static void onSignInAttemptFinished(LoginFragment loginFragment, Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account == null) {
                loginFragment.onLoginFailed();
                return;
            }
            new AsyncLoginTask(loginFragment, account).execute();
        } catch (ApiException e) {
            log.error(e.getMessage());
        }
    }

}
