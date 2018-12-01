package com.sgundersen.durak.control;

import com.sgundersen.durak.draw.OrthoCamera;
import com.sgundersen.durak.draw.Transform;

import org.joml.Vector2f;

public class OtherHandController extends HandController {

    public OtherHandController(MatchClient matchClient, OrthoCamera camera, Vector2f cardSize) {
        super(matchClient, camera, cardSize);
    }

    public void update() {
        int cards = getMatchClient().getState().getOtherPlayerHandCount();
        resize(cards);
        float totalWidth = getCamera().width();
        float centerX = totalWidth / 2.0f;
        float widthPerIndex = totalWidth / (float) cards;
        float x = centerX - widthPerIndex * 0.5f * (float) cards;
        for (Transform transform : getTransforms()) {
            transform.position.set(x, 8.0f);
            transform.size.set(getCardSize());
            x += widthPerIndex;
        }
    }

}
