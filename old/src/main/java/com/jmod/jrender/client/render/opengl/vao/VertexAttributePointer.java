package com.jmod.jrender.client.render.opengl.vao;

public class VertexAttributePointer {
    private final int index;
    private final int size;
    private final int glType;
    private final boolean normalized;
    private int stride;
    private final long offset;

    public VertexAttributePointer(int index, int size, int glType, boolean normalized, int stride, long offset) {
        this.index = index;
        this.size = size;
        this.glType = glType;
        this.normalized = normalized;
        this.stride = stride;
        this.offset = offset;
    }

    public int index() {
        return index;
    }

    public int size() {
        return size;
    }

    public int glType() {
        return glType;
    }

    public boolean normalized() {
        return normalized;
    }

    public int stride() {
        return stride;
    }

    public long offset() {
        return offset;
    }

    public void setStride(int stride) {
        this.stride = stride;
    }
}
