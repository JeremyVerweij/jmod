package com.jmod.jrender.client.model.vertex.formats.glyph.writer;

import com.jmod.jrender.client.model.vertex.fallback.VertexWriterFallback;
import com.jmod.jrender.client.model.vertex.formats.glyph.GlyphVertexSink;
import com.jmod.jrender.client.util.color.ColorABGR;
import net.minecraft.client.renderer.BufferBuilder;

public class GlyphVertexWriterFallback extends VertexWriterFallback implements GlyphVertexSink {
    public GlyphVertexWriterFallback(BufferBuilder consumer) {
        super(consumer);
    }

    @Override
    public void writeGlyph(float x, float y, float z, int color, float u, float v, int light) {
        BufferBuilder consumer = this.consumer;
        consumer.pos(x, y, z);
        consumer.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color), ColorABGR.unpackAlpha(color));
        consumer.tex(u, v);
        // TODO
        consumer.lightmap(light, light);
        consumer.endVertex();
    }
}
