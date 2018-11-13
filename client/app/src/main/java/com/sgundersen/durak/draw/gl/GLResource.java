package com.sgundersen.durak.draw.gl;

import android.opengl.GLES30;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GLResource {

    protected void verify() {
        int error = GLES30.glGetError();
        if (error != GLES30.GL_NO_ERROR) {
            log.error("GL error: {}", error);
        }
    }

}
