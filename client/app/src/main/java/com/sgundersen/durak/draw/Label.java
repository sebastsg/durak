package com.sgundersen.durak.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;

import com.sgundersen.durak.draw.gl.GLShaderProgram;
import com.sgundersen.durak.draw.gl.GLTexture;

public class Label {

    private final GLTexture texture = new GLTexture();
    private final Rectangle rectangle = new Rectangle();
    private final Transform transform = new Transform();

    public Label(String text, int size) {
        render(text, size, 0xFFEEEEEE);
    }

    private void render(String text, int size, int color) {
        TextPaint paint = new TextPaint();
        paint.setTextSize((float) size);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setShadowLayer(1.0f, 0.0f, 1.0f, Color.DKGRAY);
        int width = (int) paint.measureText(text);
        int height = size * 4;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(0);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 0, size, paint);
        texture.set(bitmap);
    }

    public void update(float x, float y) {
        transform.size.set(texture.getSize());
        transform.position.set(x - transform.size.x / 2.0f, y);
    }

    public void draw(GLShaderProgram shaderProgram) {
        shaderProgram.setModel(transform.getMatrix());
        texture.bind();
        rectangle.bind();
        rectangle.draw();
    }

}
