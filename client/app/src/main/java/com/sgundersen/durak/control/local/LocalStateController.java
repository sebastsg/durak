package com.sgundersen.durak.control.local;

import com.sgundersen.durak.control.StateController;

public class LocalStateController extends StateController {

    public LocalStateController() {
        super(new LocalMatchClient());
    }

}
