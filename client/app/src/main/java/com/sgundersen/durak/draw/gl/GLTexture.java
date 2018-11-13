package com.sgundersen.durak.draw.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import org.joml.Vector2i;

import lombok.Getter;

public class GLTexture extends GLResource {

    private int textureId;

    @Getter
    private int width = 0;

    @Getter
    private int height = 0;

    public GLTexture() {
        generate();
        bind();
        setParameters();
    }

    public GLTexture(Context context, int resourceId) {
        this();
        set(BitmapFactory.decodeResource(context.getResources(), resourceId, new BitmapFactory.Options()));
    }

    public GLTexture(int color) {
        this();
        clear(color);
    }

    private void generate() {
        int[] buffer = new int[1];
        GLES30.glGenTextures(1, buffer, 0);
        verify();
        textureId = buffer[0];
    }

    private void setParameters() {
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
        verify();
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        verify();
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        verify();
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        verify();
    }

    public void bind() {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        verify();
    }

    public void set(Bitmap bitmap) {
        bind();
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        width = bitmap.getWidth() / 3;
        height = bitmap.getHeight() / 3; // todo: find appropriate method if one exists
    }

    public Vector2i getSize() {
        return new Vector2i(width, height);
    }

    public void clear(int color) {
        Bitmap bitmap = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(color);
        set(bitmap);
    }

}
