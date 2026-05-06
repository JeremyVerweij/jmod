package com.jmod.jrender.client.model.vertex.formats.screen_quad.writer;

import com.jmod.jrender.client.model.vertex.VanillaVertexTypes;
import com.jmod.jrender.client.model.vertex.buffer.VertexBufferView;
import com.jmod.jrender.client.model.vertex.buffer.VertexBufferWriterNio;
import com.jmod.jrender.client.model.vertex.formats.screen_quad.BasicScreenQuadVertexSink;

import java.nio.ByteBuffer;

public class BasicScreenQuadVertexBufferWriterNio extends VertexBufferWriterNio implements BasicScreenQuadVertexSink {
    public BasicScreenQuadVertexBufferWriterNio(VertexBufferView backingBuffer) {
        super(backingBuffer, VanillaVertexTypes.BASIC_SCREEN_QUADS);
    }

    @Override
    public void writeQuad(float x, float y, float z, int color) {
        int i = this.writeOffset;

        ByteBuffer buf = this.byteBuffer;
        buf.putFloat(i, x);
        buf.putFloat(i + 4, y);
        buf.putFloat(i + 8, z);
        buf.putInt(i + 12, color);

        this.advance();
    }
}
