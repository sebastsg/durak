package com.sgundersen.durak.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.sgundersen.durak.MainActivity;

import lombok.Getter;

public class MainActivityFragment extends Fragment {

    @Getter
    private MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    protected void showAlert(String message) {
        new AlertDialog.Builder(mainActivity)
                .setMessage(message)
                .setTitle("Info")
                .create()
                .show();
    }

}
