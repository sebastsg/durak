package com.sgundersen.durak.control.playback;

import com.sgundersen.durak.core.match.Hand;
import com.sgundersen.durak.draw.OrthoCamera;
import com.sgundersen.durak.draw.Transform;
import com.sgundersen.durak.control.HandController;
import com.sgundersen.durak.control.MatchClient;

import org.joml.Vector2f;

public class RecordedHandController extends HandController {

    public RecordedHandController(MatchClient matchClient, OrthoCamera camera, Vector2f cardSize) {
        super(matchClient, camera, cardSize);
    }

    public void update() {
        Hand hand = getMatchClient().getState().getHand();
        resize(hand.count());
        float x = 20.0f;
        float y = getCamera().height() - getCardSize().y - 8.0f;
        for (int i = 0; i < hand.count(); i++) {
            Transform transform = getTransform(i);
            transform.position.set(x, y);
            transform.size.set(getCardSize());
            x += transform.size.x * 1.05f;
        }
    }

}
