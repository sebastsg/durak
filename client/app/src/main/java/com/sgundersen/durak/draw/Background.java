package com.sgundersen.durak.draw;

import android.content.Context;

import com.sgundersen.durak.R;
import com.sgundersen.durak.draw.gl.GLShaderProgram;
import com.sgundersen.durak.draw.gl.GLTexture;

public class Background {

    private final Transform transform = new Transform();
    private final Rectangle rectangle = new Rectangle();
    private final GLTexture texture;

    public Background(Context context) {
        texture = new GLTexture(context, R.drawable.background);
    }

    public void draw(GLShaderProgram shaderProgram, OrthoCamera camera) {
        transform.size.set(camera.width(), camera.height());
        shaderProgram.setModel(transform.getMatrix());
        texture.bind();
        rectangle.bind();
        rectangle.draw();
    }

}
