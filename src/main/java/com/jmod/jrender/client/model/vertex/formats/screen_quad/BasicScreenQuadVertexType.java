package com.jmod.jrender.client.model.vertex.formats.screen_quad;

import com.jmod.jrender.client.model.vertex.buffer.VertexBufferView;
import com.jmod.jrender.client.model.vertex.formats.screen_quad.writer.BasicScreenQuadVertexBufferWriterNio;
import com.jmod.jrender.client.model.vertex.formats.screen_quad.writer.BasicScreenQuadVertexBufferWriterUnsafe;
import com.jmod.jrender.client.model.vertex.formats.screen_quad.writer.BasicScreenQuadVertexWriterFallback;
import com.jmod.jrender.client.model.vertex.type.BlittableVertexType;
import com.jmod.jrender.client.model.vertex.type.VanillaVertexType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class BasicScreenQuadVertexType implements VanillaVertexType<BasicScreenQuadVertexSink>, BlittableVertexType<BasicScreenQuadVertexSink> {
    @Override
    public BasicScreenQuadVertexSink createFallbackWriter(BufferBuilder consumer) {
        return new BasicScreenQuadVertexWriterFallback(consumer);
    }

    @Override
    public BasicScreenQuadVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
        return direct ? new BasicScreenQuadVertexBufferWriterUnsafe(buffer) : new BasicScreenQuadVertexBufferWriterNio(buffer);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return BasicScreenQuadVertexSink.VERTEX_FORMAT;
    }

    @Override
    public BlittableVertexType<BasicScreenQuadVertexSink> asBlittable() {
        return this;
    }
}
