package com.sgundersen.durak.draw;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import lombok.Getter;
import lombok.Setter;

public class OrthoCamera {

    private static final float farClippingPlane = 1000.0f;
    private static final float nearClippingPlane = 0.01f;

    @Getter
    @Setter
    private float zoom = 1.0f;

    private Vector2f position = new Vector2f(0.0f);
    private Vector2f size = new Vector2f(0.0f);

    public void setSize(float width, float height) {
        this.size.set(width, height);
    }

    public Matrix4f getView() {
        float x = (float)Math.ceil(position.x);
        float y = (float)Math.ceil(position.y);
        Matrix4f view = new Matrix4f();
        view.translate(-x, -y, -1.0f);
        view.scale(zoom, zoom, zoom);
        return view;
    }

    public Matrix4f getProjection() {
        Matrix4f projection = new Matrix4f();
        projection.ortho(0.0f, size.x, size.y, 0.0f, nearClippingPlane, farClippingPlane);
        return projection;
    }

    public float x() {
        return position.x / zoom;
    }

    public float y() {
        return position.y / zoom;
    }

    public float width() {
        return size.x / zoom;
    }

    public float height() {
        return size.y / zoom;
    }

}
