package com.sgundersen.durak.control.live;

import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.core.match.Hand;
import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.draw.OrthoCamera;
import com.sgundersen.durak.draw.Transform;
import com.sgundersen.durak.control.HandController;
import com.sgundersen.durak.control.MatchClient;

import org.joml.Vector2f;

import lombok.Getter;

@Getter
public class MyHandController extends HandController {

    private float cardBeingDraggedStartY = 0.0f;
    private Card cardBeingDragged;
    private int cardIndexBeingDragged = -1;
    private boolean isDragging = false;

    private float viewOffsetX = -32.0f;
    private float lastViewOffsetX = -32.0f;

    public MyHandController(MatchClient matchClient, OrthoCamera camera, Vector2f cardSize) {
        super(matchClient, camera, cardSize);
    }

    public void update() {
        Hand hand = getMatchClient().getState().getHand();
        resize(hand.count());
        float x = 20.0f;
        float y = getCamera().height() - getCardSize().y - 8.0f;
        for (int i = 0; i < hand.count(); i++) {
            Transform transform = getTransform(i);
            transform.position.x = x + viewOffsetX;
            if (hand.get(i) != cardBeingDragged) {
                transform.position.y = y;
            }
            transform.size.set(getCardSize());
            x += transform.size.x * 1.05f;
        }
    }

    private void placeCardFromHand(int cardIndex) {
        MatchClientState state = getMatchClient().getState();
        Hand hand = state.getHand();
        Card card = hand.get(cardIndex);
        if (state.isAttacking()) {
            if (!state.getBout().canAttack(card)) {
                return;
            }
            getMatchClient().useCard(cardIndex);
            hand.take(cardIndex);
        } else if (state.isDefending()) {
            if (!state.getBout().canDefend(card)) {
                return;
            }
            getMatchClient().useCard(cardIndex);
            hand.take(cardIndex);
        }
    }

    private int getIntersectingCard(Vector2f position) {
        Hand hand = getMatchClient().getState().getHand();
        for (int i = 0; i < hand.count(); i++) {
            if (getTransform(i).intersects(position)) {
                return i;
            }
        }
        return -1;
    }

    public void startDrag(Vector2f position) {
        cardIndexBeingDragged = getIntersectingCard(position);
        if (cardIndexBeingDragged != -1) {
            Hand hand = getMatchClient().getState().getHand();
            cardBeingDragged = hand.get(cardIndexBeingDragged);
            cardBeingDraggedStartY = getTransform(cardIndexBeingDragged).position.y;
            isDragging = true;
        }
    }

    public void continueDrag(Vector2f delta) {
        if (!isDragging) {
            return;
        }
        Hand hand = getMatchClient().getState().getHand();
        if (hand.isEmpty()) {
            return;
        }
        if (Math.abs(delta.x) > Math.abs(delta.y)) {
            float totalWidth = hand.count() * getCardSize().x;
            if (lastViewOffsetX + delta.x < -totalWidth / 2.0f) {
                return;
            }
            if (lastViewOffsetX + delta.x > totalWidth / 2.0f) {
                return;
            }
            viewOffsetX = lastViewOffsetX + delta.x;
            cardBeingDragged = null;
            cardIndexBeingDragged = -1;
            cardBeingDraggedStartY = 0.0f;
        } else {
            if (cardBeingDragged != null && delta.y < 0.0f) {
                float offset = Math.max(-getCardSize().y * 2.0f, delta.y);
                getTransform(cardIndexBeingDragged).position.y = cardBeingDraggedStartY + offset;
            }
        }
    }

    public void endDrag() {
        if (cardBeingDragged != null) {
            float delta = cardBeingDraggedStartY - getTransform(cardIndexBeingDragged).position.y;
            float threshold = getCardSize().y * 1.5f;
            if (delta > threshold) {
                placeCardFromHand(cardIndexBeingDragged);
            }
        }
        cardBeingDragged = null;
        cardIndexBeingDragged = -1;
        cardBeingDraggedStartY = 0.0f;
        lastViewOffsetX = viewOffsetX;
        isDragging = false;
    }

}
