package com.sgundersen.durak;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.sgundersen.durak.ui.LoginView;
import com.sgundersen.durak.ui.OnBottomNavigationItemSelectedListener;
import com.sgundersen.durak.ui.TabView;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainActivity extends AppCompatActivity {

    @Getter
    private ConstraintLayout layout;

    private TabView tabView;
    private BottomNavigationView navigationView;
    private LoginView loginView;

    public void showAlert(String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(message).setTitle("Info");
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.container);
        loginView = new LoginView(this);
        loginView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001 && loginView != null) {
            loginView.onSignInAttemptFinished(data);
        }
    }

    public void showBottomNavigation() {
        if (navigationView != null) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        ConstraintLayout navigationLayout = (ConstraintLayout)inflater.inflate(R.layout.view_bottom_navigation,null, false);
        navigationView = navigationLayout.findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new OnBottomNavigationItemSelectedListener(this));
        navigationLayout.removeView(navigationView);
        layout.addView(navigationView);
    }

    public void hideBottomNavigation() {
        if (navigationView == null) {
            return;
        }
        layout.removeView(navigationView);
        navigationView = null;
    }

    public boolean setTabView(TabView tabView) {
        if (this.tabView != null) {
            if (tabView == null) {
                this.tabView.hide();
                return false;
            }
            if (this.tabView.getClass().equals(tabView.getClass())) {
                return false;
            }
            this.tabView.hide();
        }
        this.tabView = tabView;
        if (tabView == null) {
            return false;
        }
        this.tabView.show();
        showBottomNavigation();
        return true;
    }

}
