package com.jmod.jrender.client.gl.buffer;

import com.jmod.jrender.client.gl.attribute.GlVertexFormat;

import java.nio.ByteBuffer;

/**
 * Helper type for tagging the vertex format alongside the raw buffer data.
 */
public class VertexData {
    public final GlVertexFormat<?> format;
    public final ByteBuffer buffer;

    public VertexData(ByteBuffer buffer, GlVertexFormat<?> format) {
        this.format = format;
        this.buffer = buffer;
    }
}
