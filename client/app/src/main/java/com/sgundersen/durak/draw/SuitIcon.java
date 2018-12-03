package com.sgundersen.durak.draw;

import android.content.Context;

import com.sgundersen.durak.R;
import com.sgundersen.durak.core.match.CardSuit;
import com.sgundersen.durak.draw.gl.GLShaderProgram;
import com.sgundersen.durak.draw.gl.GLTexture;

public class SuitIcon {

    private final GLTexture texture;
    private final Transform transform = new Transform();
    private final Rectangle rectangle = new Rectangle();

    public SuitIcon(Context context, CardSuit suit) {
        texture = new GLTexture(context, R.drawable.suits);
        float u = (float) suit.ordinal() * 0.25f;
        rectangle.setTexCoords(u, 0.0f, u + 0.25f, 1.0f);
    }

    public void update(float right, float bottom) {
        transform.size.x = (float) texture.getWidth() * 0.25f * 1.5f;
        transform.size.y = (float) texture.getHeight() * 1.5f;
        transform.position.x = right - transform.size.x;
        transform.position.y = bottom - transform.size.y;
    }

    public void draw(GLShaderProgram shaderProgram) {
        shaderProgram.setModel(transform.getMatrix());
        texture.bind();
        rectangle.bind();
        rectangle.draw();
    }

}
