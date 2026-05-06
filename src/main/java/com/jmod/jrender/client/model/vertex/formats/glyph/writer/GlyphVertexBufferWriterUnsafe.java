package com.jmod.jrender.client.model.vertex.formats.glyph.writer;

import com.jmod.jrender.client.model.vertex.VanillaVertexTypes;
import com.jmod.jrender.client.model.vertex.buffer.VertexBufferView;
import com.jmod.jrender.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.jmod.jrender.client.model.vertex.formats.glyph.GlyphVertexSink;
import com.jmod.jrender.client.util.CompatMemoryUtil;

public class GlyphVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements GlyphVertexSink {
    public GlyphVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
        super(backingBuffer, VanillaVertexTypes.GLYPHS);
    }

    @Override
    public void writeGlyph(float x, float y, float z, int color, float u, float v, int light) {
        long i = this.writePointer;

        CompatMemoryUtil.memPutFloat(i, x);
        CompatMemoryUtil.memPutFloat(i + 4, y);
        CompatMemoryUtil.memPutFloat(i + 8, z);
        CompatMemoryUtil.memPutInt(i + 12, color);
        CompatMemoryUtil.memPutFloat(i + 16, u);
        CompatMemoryUtil.memPutFloat(i + 20, v);
        CompatMemoryUtil.memPutInt(i + 24, light);

        this.advance();

    }
}
