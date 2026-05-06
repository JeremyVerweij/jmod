package com.jmod.jrender.client.gl.attribute;

import net.minecraft.client.renderer.vertex.VertexFormat;

public interface BufferVertexFormat {
    static BufferVertexFormat from(VertexFormat format) {
        return (BufferVertexFormat) format;
    }

    int getStride();
}
