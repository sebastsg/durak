package com.sgundersen.durak.ui.rules;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgundersen.durak.R;
import com.sgundersen.durak.ui.MainActivityFragment;

public class RulesFragment extends MainActivityFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_rules, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().showNavigationFragment();
    }

}
