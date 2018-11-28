package com.sgundersen.durak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sgundersen.durak.net.auth.GoogleLogin;
import com.sgundersen.durak.ui.LoginFragment;
import com.sgundersen.durak.ui.MainActivityFragment;
import com.sgundersen.durak.ui.NavigationFragment;

public class MainActivity extends AppCompatActivity {

    private MainActivityFragment mainFragment;
    private MainActivityFragment navigationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainFragment(new LoginFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoogleLogin.RC_SIGN_IN) {
            GoogleLogin.onSignInAttemptFinished((LoginFragment) mainFragment, data);
        }
    }

    public boolean setMainFragment(MainActivityFragment fragment) {
        mainFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, mainFragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    public void showNavigationFragment() {
        if (navigationFragment != null) {
            return;
        }
        navigationFragment = new NavigationFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navigation_fragment, navigationFragment)
                .commit();
    }

    public void hideNavigationFragment() {
        if (navigationFragment == null) {
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .remove(navigationFragment)
                .commit();
        navigationFragment = null;
    }

}
