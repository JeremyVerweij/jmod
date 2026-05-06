package com.jmod.jrender.client.model.vertex.formats.particle.writer;

import com.jmod.jrender.client.model.vertex.fallback.VertexWriterFallback;
import com.jmod.jrender.client.model.vertex.formats.particle.ParticleVertexSink;
import com.jmod.jrender.client.util.color.ColorABGR;
import net.minecraft.client.renderer.BufferBuilder;

public class ParticleVertexWriterFallback extends VertexWriterFallback implements ParticleVertexSink {
    public ParticleVertexWriterFallback(BufferBuilder consumer) {
        super(consumer);
    }

    @Override
    public void writeParticle(float x, float y, float z, float u, float v, int color, int light) {
        BufferBuilder consumer = this.consumer;
        consumer.pos(x, y, z);
        consumer.tex(u, v);
        consumer.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color), ColorABGR.unpackAlpha(color));
        // TODO
        consumer.lightmap(light, light);
        consumer.endVertex();
    }
}
