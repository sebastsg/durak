package com.sgundersen.durak.draw;

import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.control.MatchClient;

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

    private void resize() {
        while (matchClient.getState().getTalonCardCount() > transforms.size()) {
            transforms.add(new Transform());
        }
        while (transforms.size() > matchClient.getState().getTalonCardCount()) {
            transforms.remove(0);
        }
    }

    public void update() {
        resize();
        float centerY = camera.height() / 2.0f;
        if (matchClient.getState().getTalonCardCount() > 0) {
            bottomTransform.size.set(cardRenderer.getCardSize());
            bottomTransform.position.x = 12.0f + bottomTransform.size.x * 0.5f;
            bottomTransform.position.y = centerY - bottomTransform.size.y / 2.0f;
            bottomTransform.angle = 95.0f;
        }
        for (int i = 0; i < matchClient.getState().getTalonCardCount() - 1; i++) {
            Transform transform = transforms.get(i);
            transform.size.set(cardRenderer.getCardSize());
            transform.position.x = 12.0f + (float)i;
            transform.position.y = centerY + (float)i - transform.size.y / 2.0f;
            transform.angle = 5.0f + (float)i * 0.1f;
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
