package com.sgundersen.durak.draw;

import com.sgundersen.durak.core.match.Bout;
import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.control.BoutController;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BoutRenderer {

    private final CardRenderer cardRenderer;
    private final BoutController controller;

    public void draw() {
        Bout bout = controller.getMatchClient().getState().getBout();
        List<Card> finishedCards = bout.getFinishedCards();
        cardRenderer.bind();
        for (int i = 0; i < finishedCards.size(); i++) {
            cardRenderer.draw(finishedCards.get(i), controller.getFinishedTransform(i));
        }
        if (bout.isAttackerPresent()) {
            cardRenderer.draw(bout.getAttackingCard(), controller.getAttackingTransform());
        }
    }

}
