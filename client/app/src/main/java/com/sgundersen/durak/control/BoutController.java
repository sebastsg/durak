package com.sgundersen.durak.control;

import com.sgundersen.durak.core.match.Bout;
import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.core.net.match.MatchClientState;
import com.sgundersen.durak.draw.OrthoCamera;
import com.sgundersen.durak.draw.Transform;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoutController {

    private final Transform attackingTransform = new Transform();
    private final List<Transform> finishedTransforms = new ArrayList<>();
    private final MatchClient matchClient;
    private final OrthoCamera camera;
    private final Vector2f cardSize;

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
        float x = 48.0f + dragStartX;
        float y = 352.0f + dragStartY;
        boolean isPortraitMode = camera.height() > camera.width();
        if (!isPortraitMode) {
            y -= 128.0f; // To avoid resizing cards. A bit lazy approach, but works for now.
        }
        for (int i = 0; i < finishedCards.size(); i += 2) {
            Transform attacker = finishedTransforms.get(i);
            Transform defender = finishedTransforms.get(i + 1);
            attacker.size.set(cardSize);
            defender.size.set(cardSize);
            attacker.position.set(x, y);
            defender.position.set(x + cardSize.x * 0.3f, y + cardSize.y * 0.3f);
            x += cardSize.x * 1.35f;
            if (i == 4 && isPortraitMode) {
                // 3 attacks are finished, so we skip to the next row with some left padding
                x = 64.0f + dragStartX;
                y += cardSize.y * 1.35f;
            }
        }
        if (bout.isAttackerPresent()) {
            attackingTransform.position.set(x, y);
            attackingTransform.size.set(cardSize);
        }
    }

    public Transform getFinishedTransform(int index) {
        return finishedTransforms.get(index);
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
                dragStartY = Math.min(cardSize.y * 2.0f, delta.y);
            }
        } else if (state.isAttacking()) {
            if (Math.abs(delta.x) > Math.abs(delta.y) && !bout.isAttackerPresent()) {
                if (dragStartX > 0.0f) {
                    dragStartX = Math.min(cardSize.x * 3.0f, delta.x);
                } else {
                    dragStartX = Math.max(-cardSize.x * 3.0f, delta.x);
                }
            }
        }
    }

    public void endDrag() {
        if (!isDragging) {
            return;
        }
        if (matchClient.getState().isDefending()) {
            float threshold = cardSize.y * 1.5f;
            if (dragStartY > threshold) {
                matchClient.takeCards();
            }
        } else if (matchClient.getState().isAttacking()) {
            float threshold = cardSize.x * 2.5f;
            if (Math.abs(dragStartX) > threshold) {
                matchClient.endTurn();
            }
        }
        isDragging = false;
        dragStartX = 0.0f;
        dragStartY = 0.0f;
    }

}
