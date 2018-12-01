package com.sgundersen.durak.draw;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.sgundersen.durak.R;
import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.draw.gl.GLShaderProgram;
import com.sgundersen.durak.draw.gl.GLTexture;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class CardRenderer {

    // One extra for the back side.
    private static int MAX_CARDS = 53;

    private final List<Rectangle> rectangles = new ArrayList<>();
    private final GLTexture texture;
    private final GLShaderProgram shaderProgram;

    @Getter
    private final Vector2f cardSize;

    public CardRenderer(Context context, GLShaderProgram shaderProgram, Vector2f cardSize, int maxX, int maxY) {
        this.cardSize = cardSize;
        this.shaderProgram = shaderProgram;
        texture = new GLTexture();
        texture.set(BitmapFactory.decodeResource(context.getResources(), R.drawable.cards, new BitmapFactory.Options()));
        float u2 = cardSize.x / (float)texture.getWidth();
        float v2 = cardSize.y / (float)texture.getHeight();
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (x * y >= MAX_CARDS) {
                    break;
                }
                float u1 = ((float)x * cardSize.x) / (float)texture.getWidth();
                float v1 = ((float)y * cardSize.y) / (float)texture.getHeight();
                Rectangle rectangle = new Rectangle();
                rectangle.setTexCoords(u1, v1, u1 + u2, v1 + v2);
                rectangles.add(rectangle);
            }
        }
    }

    public void bind() {
        texture.bind();
    }

    public void draw(Card card, Transform transform) {
        shaderProgram.setModel(transform.getMatrix());
        Rectangle rectangle = rectangles.get(card.getIndex());
        rectangle.bind();
        rectangle.draw();
    }

    public void drawBackside(Transform transform) {
        shaderProgram.setModel(transform.getMatrix());
        Rectangle rectangle = rectangles.get(MAX_CARDS - 1);
        rectangle.bind();
        rectangle.draw();
    }

}
