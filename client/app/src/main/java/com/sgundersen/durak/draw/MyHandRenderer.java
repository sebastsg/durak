package com.sgundersen.durak.draw;

import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.core.match.Hand;
import com.sgundersen.durak.match.MatchClient;

import org.joml.Vector2f;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyHandRenderer extends HandRenderer {

    private final CardRenderer cardRenderer;
    private final MatchClient matchClient;
    private final OrthoCamera camera;

    private float cardBeingDraggedStartY = 0.0f;
    private Card cardBeingDragged;
    private int cardIndexBeingDragged = -1;
    private boolean isDragging = false;

    private float viewOffsetX = -32.0f;
    private float lastViewOffsetX = -32.0f;

    public MyHandRenderer(CardRenderer cardRenderer, MatchClient matchClient, OrthoCamera camera) {
        this.cardRenderer = cardRenderer;
        this.matchClient = matchClient;
        this.camera = camera;
    }

    @Override
    public void update() {
        Hand hand = matchClient.getState().getHand();
        resize(hand.count());
        float x = 20.0f;
        float y = camera.height() - cardRenderer.getCardSize().y - 8.0f;
        for (int i = 0; i < hand.count(); i++) {
            Transform transform = transforms.get(i);
            transform.position.x = x + viewOffsetX;
            if (hand.get(i) != cardBeingDragged) {
                transform.position.y = y;
            }
            transform.size.set(cardRenderer.getCardSize());
            x += transform.size.x * 1.05f;
        }
    }

    @Override
    public void draw() {
        Hand hand = matchClient.getState().getHand();
        cardRenderer.bind();
        for (int i = 0; i < hand.count(); i++) {
            cardRenderer.draw(hand.get(i), transforms.get(i));
        }
    }

    private void placeCardFromHand(int cardIndex) {
        Hand hand = matchClient.getState().getHand();
        Card card = hand.get(cardIndex);
        if (matchClient.getState().isAttacking()) {
            if (!matchClient.getState().getBout().canAttack(card)) {
                log.info("Dragged card {} can not attack here.", card.getFullName());
                return;
            }
            matchClient.useCard(cardIndex);
            hand.take(cardIndex);
        } else if (matchClient.getState().isDefending()) {
            if (!matchClient.getState().getBout().canDefend(card)) {
                log.info("Dragged card {} can not defend against this.", card.getFullName());
                return;
            }
            matchClient.useCard(cardIndex);
            hand.take(cardIndex);
        } else {
            log.warn("Not attacking or defending. Cannot use card.");
        }
    }

    private int getIntersectingCard(Vector2f position) {
        Hand hand = matchClient.getState().getHand();
        for (int i = 0; i < hand.count(); i++) {
            if (transforms.get(i).intersects(position)) {
                return i;
            }
        }
        return -1;
    }

    public void startDrag(Vector2f position) {
        cardIndexBeingDragged = getIntersectingCard(position);
        if (cardIndexBeingDragged != -1) {
            Hand hand = matchClient.getState().getHand();
            cardBeingDragged = hand.get(cardIndexBeingDragged);
            cardBeingDraggedStartY = transforms.get(cardIndexBeingDragged).position.y;
            isDragging = true;
        }
    }

    public void continueDrag(Vector2f delta) {
        if (!isDragging) {
            return;
        }
        Hand hand = matchClient.getState().getHand();
        if (hand.isEmpty()) {
            return;
        }
        if (Math.abs(delta.x) > Math.abs(delta.y)) {
            float totalWidth = hand.count() * cardRenderer.getCardSize().x;
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
                float offset = Math.max(-cardRenderer.getCardSize().y * 2.0f, delta.y);
                transforms.get(cardIndexBeingDragged).position.y = cardBeingDraggedStartY + offset;
            }
        }
    }

    public void endDrag() {
        if (cardBeingDragged != null) {
            float delta = cardBeingDraggedStartY - transforms.get(cardIndexBeingDragged).position.y;
            float threshold = cardRenderer.getCardSize().y * 1.5f;
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
