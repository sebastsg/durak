package com.sgundersen.durak.draw.gl;

import android.content.Context;
import android.opengl.GLES30;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GLShaderProgram extends GLResource {

    private class GLShader extends GLResource {

        @Getter
        private final int shaderId;

        public GLShader(Context context, int resourceId, int type) {
            shaderId = GLES30.glCreateShader(type);
            verify();
            InputStream stream = context.getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            List<String> lines = new ArrayList<>();
            String nextLine;
            do {
                try {
                    nextLine = reader.readLine();
                } catch (IOException e) {
                    log.error(e.getMessage());
                    return;
                }
                if (nextLine != null) {
                    lines.add(nextLine);
                }
            } while (nextLine != null);
            StringBuilder source = new StringBuilder();
            for (String line : lines) {
                source.append(line).append("\n");
            }
            GLES30.glShaderSource(shaderId, source.toString());
            verify();
            GLES30.glCompileShader(shaderId);
            verify();
            log.info("Shader info log: {}", GLES30.glGetShaderInfoLog(shaderId));
        }

    }

    private final int programId;
    private Matrix4f model = new Matrix4f();
    private Matrix4f view = new Matrix4f();
    private Matrix4f projection = new Matrix4f();
    private Matrix4f modelViewProjection = new Matrix4f();
    private Vector4f color = new Vector4f(1.0f);

    private final int colorLocation;
    private final int modelViewProjectionLocation;

    public GLShaderProgram(Context context, int vertexResource, int fragmentResource, String[] attributes) {
        programId = GLES30.glCreateProgram();
        verify();
        GLShader vertexShader = new GLShader(context, vertexResource, GLES30.GL_VERTEX_SHADER);
        GLES30.glAttachShader(programId, vertexShader.getShaderId());
        verify();
        for (int location = 0; location < attributes.length; location++) {
            GLES30.glBindAttribLocation(programId, location, attributes[location]);
            verify();
        }
        GLShader fragmentShader = new GLShader(context, fragmentResource, GLES30.GL_FRAGMENT_SHADER);
        GLES30.glAttachShader(programId, fragmentShader.getShaderId());
        verify();
        GLES30.glLinkProgram(programId);
        verify();
        log.info("Info log: {}", GLES30.glGetProgramInfoLog(programId));
        verify();
        GLES30.glValidateProgram(programId);
        verify();
        int[] pr = new int[1];
        GLES30.glGetProgramiv(programId, GLES30.GL_VALIDATE_STATUS, pr, 0);
        verify();
        log.info("Validation status: {}", pr[0]);
        if (pr[0] == 0) {
            log.error("Validation has failed.");
        }

        modelViewProjectionLocation = GLES30.glGetUniformLocation(programId, "uni_ModelViewProjection");
        verify();
        colorLocation = GLES30.glGetUniformLocation(programId, "uni_Color");
        verify();
    }

    public void bind() {
        GLES30.glUseProgram(programId);
        verify();
    }

    private void setModelViewProjection() {
        FloatBuffer modelViewProjectionBuffer = FloatBuffer.allocate(16);
        modelViewProjection = new Matrix4f(projection);
        modelViewProjection.mul(view);
        modelViewProjection.mul(model);
        modelViewProjection.get(modelViewProjectionBuffer);
        modelViewProjectionBuffer.position(0);
        GLES30.glUniformMatrix4fv(modelViewProjectionLocation, 1, false, modelViewProjectionBuffer);
        verify();
    }

    public void setViewProjection(Matrix4f view, Matrix4f projection) {
        this.view = view;
        this.projection = projection;
        setModelViewProjection();
    }

    public void setModel(Matrix4f model) {
        this.model = model;
        setModelViewProjection();
    }

    public void setColor(Vector4f color) {
        this.color = color;
        FloatBuffer colorBuffer = FloatBuffer.allocate(4);
        color.get(colorBuffer);
        colorBuffer.position(0);
        GLES30.glUniform4fv(colorLocation, 1, colorBuffer);
        verify();
    }

}
