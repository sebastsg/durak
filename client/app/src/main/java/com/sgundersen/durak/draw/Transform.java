package com.sgundersen.durak.draw;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Transform {

    public Vector2f position = new Vector2f(0.0f);
    public Vector2f size = new Vector2f(0.0f);
    public float angle = 0.0f;

    public Matrix4f getMatrix() {
        Vector2f origin = new Vector2f(position.x + size.x / 2.0f, position.y + size.y / 2.0f);
        Matrix4f matrix = new Matrix4f();
        matrix.translate(origin.x, origin.y, 0.0f);
        matrix.rotate((float)Math.toRadians(-angle), 0.0f, 0.0f, 1.0f);
        matrix.translate(-origin.x, -origin.y, 0.0f);
        matrix.translate(position.x, position.y, 0.0f);
        matrix.scale(size.x, size.y, 0.0f);
        return matrix;
    }

    public boolean intersects(Vector2f otherPosition) {
        if (otherPosition.x < position.x || otherPosition.y < position.y) {
            return false;
        }
        if (otherPosition.x > position.x + size.x || otherPosition.y > position.y + size.y) {
            return false;
        }
        return true;
    }

}
