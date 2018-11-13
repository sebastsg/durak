package com.sgundersen.durak.draw.gl;

import android.opengl.GLES30;

import com.sgundersen.durak.draw.Vertex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GLVertexArray extends GLResource {

    private int vertexArrayId;
    private GLBuffer vertexBuffer;
    private GLBuffer indexBuffer;
    private int indexCount = 0;

    public GLVertexArray(int[] attributes) {
        vertexBuffer = new GLBuffer(GLES30.GL_ARRAY_BUFFER);
        indexBuffer = new GLBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER);
        generate();
        bind();
        setAttributes(attributes);
    }

    private void generate() {
        int vertexArrayIds[] = new int[1];
        GLES30.glGenVertexArrays(1, vertexArrayIds, 0);
        verify();
        vertexArrayId = vertexArrayIds[0];
    }

    private void setAttributes(int[] attributes) {
        int vertexSize = 0;
        for (int attributeCount : attributes) {
            vertexSize += attributeCount * Float.BYTES;
        }
        int offset = 0;
        for (int attribute = 0; attribute < attributes.length; attribute++) {
            GLES30.glVertexAttribPointer(attribute, attributes[attribute], GLES30.GL_FLOAT, false, vertexSize, offset);
            verify();
            GLES30.glEnableVertexAttribArray(attribute);
            verify();
            offset += attributes[attribute] * Float.BYTES;
        }
    }

    public void bind() {
        GLES30.glBindVertexArray(vertexArrayId);
        verify();
        indexBuffer.bind();
        vertexBuffer.bind();
    }

    public void draw() {
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indexCount, GLES30.GL_UNSIGNED_SHORT, 0);
        verify();
    }

    public void setIndices(short[] indices) {
        indexCount = indices.length;
        if (indices.length == 0) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES * indices.length);
        buffer.order(ByteOrder.nativeOrder());
        for (short index : indices) {
            buffer.putShort(index);
        }
        buffer.position(0);
        indexBuffer.set(buffer);
    }

    public void setVertices(Vertex[] vertices) {
        if (vertices.length == 0) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(vertices[0].size() * vertices.length);
        buffer.order(ByteOrder.nativeOrder());
        for (Vertex vertex : vertices) {
            buffer.putFloat(vertex.position.x);
            buffer.putFloat(vertex.position.y);
            buffer.putFloat(vertex.texCoords.x);
            buffer.putFloat(vertex.texCoords.y);
        }
        buffer.position(0);
        vertexBuffer.set(buffer);
    }

}
