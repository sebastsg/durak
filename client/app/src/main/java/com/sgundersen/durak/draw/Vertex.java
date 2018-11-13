package com.sgundersen.durak.draw;

import org.joml.Vector2f;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Vertex {

    public Vector2f position = new Vector2f();
    public Vector2f texCoords = new Vector2f();

    public int size() {
        return 4 * Float.BYTES;
    }

}
