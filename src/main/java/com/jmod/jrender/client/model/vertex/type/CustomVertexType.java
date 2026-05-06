package com.jmod.jrender.client.model.vertex.type;

import com.jmod.jrender.client.gl.attribute.BufferVertexFormat;
import com.jmod.jrender.client.gl.attribute.GlVertexFormat;
import com.jmod.jrender.client.model.vertex.VertexSink;

public interface CustomVertexType<T extends VertexSink, A extends Enum<A>> extends BufferVertexType<T> {
    /**
     * @return The {@link GlVertexFormat} required for blitting (direct writing into buffers)
     */
    GlVertexFormat<A> getCustomVertexFormat();

    @Override
    default BufferVertexFormat getBufferVertexFormat() {
        return this.getCustomVertexFormat();
    }
}
