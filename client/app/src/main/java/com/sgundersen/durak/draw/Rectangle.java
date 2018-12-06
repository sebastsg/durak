package com.sgundersen.durak.draw;

import com.sgundersen.durak.draw.gl.GLVertexArray;

import org.joml.Vector2f;

public class Rectangle {

    private final GLVertexArray vertexArray = new GLVertexArray(new int[]{2, 2});

    public Rectangle() {
        vertexArray.setIndices(new short[]{0, 1, 2, 3, 2, 0});
        setTexCoords(0.0f, 0.0f, 1.0f, 1.0f);
    }

    public void bind() {
        vertexArray.bind();
    }

    public void draw() {
        vertexArray.draw();
    }

    public void setTexCoords(float u1, float v1, float u2, float v2) {
        vertexArray.setVertices(new Vertex[]{
                new Vertex(new Vector2f(0.0f, 0.0f), new Vector2f(u1, v1)),
                new Vertex(new Vector2f(1.0f, 0.0f), new Vector2f(u2, v1)),
                new Vertex(new Vector2f(1.0f, 1.0f), new Vector2f(u2, v2)),
                new Vertex(new Vector2f(0.0f, 1.0f), new Vector2f(u1, v2))
        });
    }

}
