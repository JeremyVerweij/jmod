package com.jmod.jrender.client.model.vertex.formats.line;

import com.jmod.jrender.client.model.vertex.buffer.VertexBufferView;
import com.jmod.jrender.client.model.vertex.formats.line.writer.LineVertexBufferWriterNio;
import com.jmod.jrender.client.model.vertex.formats.line.writer.LineVertexBufferWriterUnsafe;
import com.jmod.jrender.client.model.vertex.formats.line.writer.LineVertexWriterFallback;
import com.jmod.jrender.client.model.vertex.type.BlittableVertexType;
import com.jmod.jrender.client.model.vertex.type.VanillaVertexType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class LineVertexType implements VanillaVertexType<LineVertexSink>, BlittableVertexType<LineVertexSink> {
    @Override
    public LineVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
        return direct ? new LineVertexBufferWriterUnsafe(buffer) : new LineVertexBufferWriterNio(buffer);
    }

    @Override
    public LineVertexSink createFallbackWriter(BufferBuilder consumer) {
        return new LineVertexWriterFallback(consumer);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return LineVertexSink.VERTEX_FORMAT;
    }

    @Override
    public BlittableVertexType<LineVertexSink> asBlittable() {
        return this;
    }
}
