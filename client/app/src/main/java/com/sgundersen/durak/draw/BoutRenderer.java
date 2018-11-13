package com.sgundersen.durak.draw;

import com.sgundersen.durak.core.match.Bout;
import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.core.net.MatchClientState;
import com.sgundersen.durak.match.MatchClient;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BoutRenderer {

    private final CardRenderer cardRenderer;
    private final MatchClient matchClient;
    private final OrthoCamera camera;

    private final Transform attackingTransform = new Transform();
    private final List<Transform> finishedTransforms = new ArrayList<>();

    private boolean isDragging = false;
    private float dragStartX = 0.0f;
    private float dragStartY = 0.0f;

    private void resize() {
        List<Card> finishedCards = matchClient.getState().getBout().getFinishedCards();
        while (finishedCards.size() > finishedTransforms.size()) {
            finishedTransforms.add(new Transform());
        }
        while (finishedTransforms.size() > finishedCards.size()) {
            finishedTransforms.remove(0);
        }
    }

    public void update() {
        resize();
        Bout bout = matchClient.getState().getBout();
        List<Card> finishedCards = bout.getFinishedCards();
        float x = camera.width() / 4.5f + dragStartX;
        float y = camera.height() / 4.5f + dragStartY;
        Vector2f cardSize = cardRenderer.getCardSize();
        for (int i = 0; i < finishedCards.size(); i += 2) {
            Transform attacker = finishedTransforms.get(i);
            Transform defender = finishedTransforms.get(i + 1);
            attacker.size.set(cardSize);
            defender.size.set(cardSize);
            attacker.position.set(x, y);
            defender.position.set(x + cardSize.x * 0.3f, y + cardSize.y * 0.3f);
            x += cardSize.x * 1.3;
        }
        if (bout.isAttackerPresent()) {
            attackingTransform.position.set(x, y);
            attackingTransform.size.set(cardRenderer.getCardSize());
        }
    }

    public void draw() {
        Bout bout = matchClient.getState().getBout();
        List<Card> finishedCards = bout.getFinishedCards();
        cardRenderer.bind();
        for (int i = 0; i < finishedCards.size(); i++) {
            cardRenderer.draw(finishedCards.get(i), finishedTransforms.get(i));
        }
        if (bout.isAttackerPresent()) {
            cardRenderer.draw(bout.getAttackingCard(), attackingTransform);
        }
    }

    private boolean intersects(Vector2f position) {
        for (Transform transform : finishedTransforms) {
            if (transform.intersects(position)) {
                return true;
            }
        }
        Bout bout = matchClient.getState().getBout();
        return bout.isAttackerPresent() && attackingTransform.intersects(position);
    }

    public void startDrag(Vector2f position) {
        if (intersects(position)) {
            isDragging = true;
            dragStartX = 0.0f;
            dragStartY = 0.0f;
        }
    }

    public void continueDrag(Vector2f delta) {
        if (!isDragging) {
            return;
        }
        MatchClientState state = matchClient.getState();
        Bout bout = state.getBout();
        if (state.isDefending()) {
            if (Math.abs(delta.y) > Math.abs(delta.x) && delta.y > 0.0f && bout.isAttackerPresent()) {
                dragStartY = Math.min(cardRenderer.getCardSize().y * 2.0f, delta.y);
            }
        } else if (state.isAttacking()) {
            if (Math.abs(delta.x) > Math.abs(delta.y) && !bout.isAttackerPresent()) {
                if (dragStartX > 0.0f) {
                    dragStartX = Math.min(cardRenderer.getCardSize().x * 3.0f, delta.x);
                } else {
                    dragStartX = Math.max(-cardRenderer.getCardSize().x * 3.0f, delta.x);
                }
            }
        }
    }

    public void endDrag() {
        if (!isDragging) {
            return;
        }
        if (matchClient.getState().isDefending()) {
            float threshold = cardRenderer.getCardSize().y * 1.5f;
            if (dragStartY > threshold) {
                matchClient.takeCards();
            }
        } else if (matchClient.getState().isAttacking()) {
            float threshold = cardRenderer.getCardSize().x * 2.5f;
            if (Math.abs(dragStartX) > threshold) {
                matchClient.endTurn();
            }
        }
        isDragging = false;
        dragStartX = 0.0f;
        dragStartY = 0.0f;
    }

}
