package com.sgundersen.durak.control;

import com.sgundersen.durak.draw.OrthoCamera;
import com.sgundersen.durak.draw.Transform;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class HandController {

    private final MatchClient matchClient;
    private final OrthoCamera camera;
    private final Vector2f cardSize;
    private final List<Transform> transforms = new ArrayList<>();

    protected void resize(int count) {
        while (count > transforms.size()) {
            transforms.add(new Transform());
        }
        while (transforms.size() > count) {
            transforms.remove(0);
        }
    }

    public Transform getTransform(int index) {
        return transforms.get(index);
    }

    public abstract void update();

}
