package com.sgundersen.durak.draw;

import com.sgundersen.durak.match.MatchClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OtherHandRenderer extends HandRenderer {

    private final CardRenderer cardRenderer;
    private final MatchClient matchClient;
    private final OrthoCamera camera;

    @Override
    public void update() {
        int cards = matchClient.getState().getOtherPlayerHandCount();
        resize(cards);

        float totalWidth = camera.width();
        float centerX = totalWidth / 2.0f;
        float widthPerIndex = totalWidth / (float) cards;
        float x = centerX - widthPerIndex * 0.5f * (float) cards;

        for (Transform transform : transforms) {
            transform.position.set(x, 8.0f);
            transform.size.set(cardRenderer.getCardSize());
            x += widthPerIndex;
        }
    }

    @Override
    public void draw() {
        cardRenderer.bind();
        for (Transform transform : transforms) {
            cardRenderer.drawBackside(transform);
        }
    }

}
