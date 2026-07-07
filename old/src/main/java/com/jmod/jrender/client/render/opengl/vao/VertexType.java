package com.jmod.jrender.client.render.opengl.vao;

import static org.lwjglx.opengl.GL11.*;

public enum VertexType {
    FLOAT(GL_FLOAT, Float.BYTES),
    BYTE(GL_BYTE, Byte.BYTES), UNSIGNED_BYTE(GL_UNSIGNED_BYTE, Byte.BYTES),
    SHORT(GL_SHORT, Short.BYTES), UNSIGNED_SHORT(GL_UNSIGNED_SHORT, Short.BYTES);

    private final int glType;
    private final int bytes;

    VertexType(int glType, int bytes) {
        this.glType = glType;
        this.bytes = bytes;
    }

    public int getBytes() {
        return bytes;
    }

    public int getGlType() {
        return glType;
    }
}
