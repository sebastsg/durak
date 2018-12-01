package com.sgundersen.durak.control;

import com.sgundersen.durak.draw.OrthoCamera;

import org.joml.Vector2f;

import lombok.Getter;

@Getter
public class MatchController {

    private final MatchClient matchClient;
    private final MyHandController myHandController;
    private final OtherHandController otherHandController;
    private final BoutController boutController;

    public MatchController(MatchClient matchClient, OrthoCamera camera, Vector2f cardSize) {
        this.matchClient = matchClient;
        myHandController = new MyHandController(matchClient, camera, cardSize);
        otherHandController = new OtherHandController(matchClient, camera, cardSize);
        boutController = new BoutController(matchClient, camera, cardSize);
    }

    public void update() {
        myHandController.update();
        otherHandController.update();
        boutController.update();
    }

    public void onTouchDown(Vector2f begin) {
        if (!matchClient.isFinished() && matchClient.isInitialized()) {
            myHandController.startDrag(begin);
            boutController.startDrag(begin);
        }
    }

    public void onTouchMove(Vector2f begin, Vector2f end, Vector2f delta) {
        if (!matchClient.isFinished() && matchClient.isInitialized()) {
            myHandController.continueDrag(delta);
            boutController.continueDrag(delta);
        }
    }

    public void onTouchUp(Vector2f begin, Vector2f end, Vector2f delta) {
        if (!matchClient.isFinished() && matchClient.isInitialized()) {
            myHandController.endDrag();
            boutController.endDrag();
        }
    }

}
