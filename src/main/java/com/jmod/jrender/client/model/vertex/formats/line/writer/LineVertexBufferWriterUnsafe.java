package com.jmod.jrender.client.model.vertex.formats.line.writer;

import com.jmod.jrender.client.util.CompatMemoryUtil;

import com.jmod.jrender.client.model.vertex.VanillaVertexTypes;
import com.jmod.jrender.client.model.vertex.buffer.VertexBufferView;
import com.jmod.jrender.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import com.jmod.jrender.client.model.vertex.formats.line.LineVertexSink;

public class LineVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements LineVertexSink {
    public LineVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
        super(backingBuffer, VanillaVertexTypes.LINES);
    }

    @Override
    public void vertexLine(float x, float y, float z, int color) {
        long i = this.writePointer;

        CompatMemoryUtil.memPutFloat(i, x);
        CompatMemoryUtil.memPutFloat(i + 4, y);
        CompatMemoryUtil.memPutFloat(i + 8, z);
        CompatMemoryUtil.memPutInt(i + 12, color);

        this.advance();
    }
}
