package com.jmod.jrender.client.model.vertex.formats.glyph;

import com.jmod.jrender.client.model.vertex.buffer.VertexBufferView;
import com.jmod.jrender.client.model.vertex.formats.glyph.writer.GlyphVertexBufferWriterNio;
import com.jmod.jrender.client.model.vertex.formats.glyph.writer.GlyphVertexBufferWriterUnsafe;
import com.jmod.jrender.client.model.vertex.formats.glyph.writer.GlyphVertexWriterFallback;
import com.jmod.jrender.client.model.vertex.type.BlittableVertexType;
import com.jmod.jrender.client.model.vertex.type.VanillaVertexType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class GlyphVertexType implements VanillaVertexType<GlyphVertexSink>, BlittableVertexType<GlyphVertexSink> {
    @Override
    public GlyphVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
        return direct ? new GlyphVertexBufferWriterUnsafe(buffer) : new GlyphVertexBufferWriterNio(buffer);
    }

    @Override
    public GlyphVertexSink createFallbackWriter(BufferBuilder consumer) {
        return new GlyphVertexWriterFallback(consumer);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return GlyphVertexSink.VERTEX_FORMAT;
    }

    @Override
    public BlittableVertexType<GlyphVertexSink> asBlittable() {
        return this;
    }
}
