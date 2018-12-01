package com.sgundersen.durak.draw;

import com.sgundersen.durak.core.match.Hand;
import com.sgundersen.durak.control.MyHandController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MyHandRenderer {

    private final CardRenderer cardRenderer;
    private final MyHandController controller;

    public void draw() {
        Hand hand = controller.getMatchClient().getState().getHand();
        cardRenderer.bind();
        for (int i = 0; i < hand.count(); i++) {
            cardRenderer.draw(hand.get(i), controller.getTransform(i));
        }
    }

}
