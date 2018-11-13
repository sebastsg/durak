package com.sgundersen.durak.draw;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class HandRenderer {

    protected final List<Transform> transforms = new ArrayList<>();

    protected void resize(int count) {
        while (count > transforms.size()) {
            transforms.add(new Transform());
        }
        while (transforms.size() > count) {
            transforms.remove(0);
        }
    }

    public abstract void update();
    public abstract void draw();

}
