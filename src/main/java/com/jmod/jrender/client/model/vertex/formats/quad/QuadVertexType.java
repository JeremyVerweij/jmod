package com.jmod.jrender.client.model.vertex.formats.quad;

import com.jmod.jrender.client.model.vertex.buffer.VertexBufferView;
import com.jmod.jrender.client.model.vertex.formats.quad.writer.QuadVertexBufferWriterNio;
import com.jmod.jrender.client.model.vertex.formats.quad.writer.QuadVertexBufferWriterUnsafe;
import com.jmod.jrender.client.model.vertex.formats.quad.writer.QuadVertexWriterFallback;
import com.jmod.jrender.client.model.vertex.type.BlittableVertexType;
import com.jmod.jrender.client.model.vertex.type.VanillaVertexType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class QuadVertexType implements VanillaVertexType<QuadVertexSink>, BlittableVertexType<QuadVertexSink> {
    @Override
    public QuadVertexSink createFallbackWriter(BufferBuilder consumer) {
        return new QuadVertexWriterFallback(consumer);
    }

    @Override
    public QuadVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
        return direct ? new QuadVertexBufferWriterUnsafe(buffer) : new QuadVertexBufferWriterNio(buffer);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return QuadVertexSink.VERTEX_FORMAT;
    }

    @Override
    public BlittableVertexType<QuadVertexSink> asBlittable() {
        return this;
    }
}
