package com.sgundersen.durak.draw.gl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;

public class GLBuffer extends GLResource {

    private int bufferId;
    private int target;

    public GLBuffer(int target) {
        this.target = target;
        generate();
    }

    private void generate() {
        int bufferIds[] = {0};
        GLES30.glGenBuffers(1, bufferIds, 0);
        verify();
        bufferId = bufferIds[0];
    }

    public void bind() {
        GLES30.glBindBuffer(target, bufferId);
        verify();
    }

    public void set(ByteBuffer buffer) {
        bind();
        GLES30.glBufferData(target, buffer.remaining(), buffer, GLES30.GL_STATIC_DRAW);
        verify();
    }

}
