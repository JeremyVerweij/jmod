package com.jmod.jrender.client.model.vertex.formats.particle.writer;

import com.jmod.jrender.client.util.CompatMemoryUtil;

import com.jmod.jrender.client.model.vertex.VanillaVertexTypes;
import com.jmod.jrender.client.model.vertex.buffer.VertexBufferView;
import com.jmod.jrender.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.jmod.jrender.client.model.vertex.formats.particle.ParticleVertexSink;

public class ParticleVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements ParticleVertexSink {
    public ParticleVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
        super(backingBuffer, VanillaVertexTypes.PARTICLES);
    }

    @Override
    public void writeParticle(float x, float y, float z, float u, float v, int color, int light) {
        long i = this.writePointer;

        CompatMemoryUtil.memPutFloat(i, x);
        CompatMemoryUtil.memPutFloat(i + 4, y);
        CompatMemoryUtil.memPutFloat(i + 8, z);
        CompatMemoryUtil.memPutFloat(i + 12, u);
        CompatMemoryUtil.memPutFloat(i + 16, v);
        CompatMemoryUtil.memPutInt(i + 20, color);
        CompatMemoryUtil.memPutInt(i + 24, light);

        this.advance();
    }
}
