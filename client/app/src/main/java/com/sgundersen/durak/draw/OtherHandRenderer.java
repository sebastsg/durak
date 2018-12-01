package com.sgundersen.durak.draw;

import com.sgundersen.durak.control.OtherHandController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OtherHandRenderer {

    private final CardRenderer cardRenderer;
    private final OtherHandController otherHandController;

    public void draw() {
        cardRenderer.bind();
        for (Transform transform : otherHandController.getTransforms()) {
            cardRenderer.drawBackside(transform);
        }
    }

}
