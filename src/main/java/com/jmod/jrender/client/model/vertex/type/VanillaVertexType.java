package com.jmod.jrender.client.model.vertex.type;

import com.jmod.jrender.client.gl.attribute.BufferVertexFormat;
import com.jmod.jrender.client.model.vertex.VertexSink;
import net.minecraft.client.renderer.vertex.VertexFormat;

public interface VanillaVertexType<T extends VertexSink> extends BufferVertexType<T> {
    default BufferVertexFormat getBufferVertexFormat() {
        return BufferVertexFormat.from(this.getVertexFormat());
    }

    VertexFormat getVertexFormat();
}
