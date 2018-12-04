package com.sgundersen.durak.draw;

import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.control.MatchClient;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TalonRenderer {

    private final CardRenderer cardRenderer;
    private final MatchClient matchClient;
    private final OrthoCamera camera;

    private final Transform bottomTransform = new Transform();
    private final List<Transform> transforms = new ArrayList<>();
    private final List<Float> jitter = new ArrayList<>();

    private void resize() {
        while (matchClient.getState().getTalonCardCount() > transforms.size()) {
            transforms.add(new Transform());
            jitter.add((float) Math.random() * 9.0f - (float) Math.random() * 6.0f);
        }
        while (transforms.size() > matchClient.getState().getTalonCardCount()) {
            transforms.remove(transforms.size() - 1);
            jitter.remove(jitter.size() - 1);
        }
    }

    public void update() {
        resize();
        Vector2f cardSize = cardRenderer.getCardSize();
        float x = camera.width() - cardSize.x * 2.0f;
        float y = camera.height() - cardSize.y * 2.1f;
        if (matchClient.getState().getTalonCardCount() > 0) {
            bottomTransform.size.set(cardSize);
            bottomTransform.position.x = x + bottomTransform.size.x * 0.5f;
            bottomTransform.position.y = y - bottomTransform.size.y / 2.0f;
            bottomTransform.angle = 95.0f;
        }
        for (int i = 0; i < matchClient.getState().getTalonCardCount() - 1; i++) {
            Transform transform = transforms.get(i);
            transform.size.set(cardSize);
            transform.position.x = x + (float) i;
            transform.position.y = y + (float) i - transform.size.y / 2.0f;
            transform.angle = 5.0f + (float) i * 0.1f + jitter.get(i);
        }
    }

    public void draw() {
        Card bottomCard = matchClient.getState().getBottomCard();
        if (bottomCard != null) {
            cardRenderer.draw(bottomCard, bottomTransform);
        }
        for (Transform transform : transforms) {
            cardRenderer.drawBackside(transform);
        }
    }

}
