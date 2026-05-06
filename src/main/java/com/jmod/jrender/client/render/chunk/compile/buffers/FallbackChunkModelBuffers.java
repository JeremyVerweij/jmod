package com.jmod.jrender.client.render.chunk.compile.buffers;

import com.jmod.jrender.client.model.quad.properties.ModelQuadFacing;
import com.jmod.jrender.client.render.chunk.data.ChunkRenderData;
import com.jmod.jrender.client.render.chunk.format.ModelVertexSink;

public class FallbackChunkModelBuffers implements ChunkModelBuffers {
    public FallbackChunkModelBuffers() {

    }

    @Override
    public ModelVertexSink getSink(ModelQuadFacing facing) {
        return null;
    }

    @Override
    public ChunkRenderData.Builder getRenderData() {
        return null;
    }
}
