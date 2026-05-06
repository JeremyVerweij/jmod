package com.jmod.jrender.client.render.chunk.compile.buffers;

import com.jmod.jrender.client.model.quad.properties.ModelQuadFacing;
import com.jmod.jrender.client.render.chunk.data.ChunkRenderData;
import com.jmod.jrender.client.render.chunk.format.ModelVertexSink;

public class BakedChunkModelBuffers implements ChunkModelBuffers {
    private final ModelVertexSink[] builders;
    private final ChunkRenderData.Builder renderData;

    public BakedChunkModelBuffers(ModelVertexSink[] builders, ChunkRenderData.Builder renderData) {
        this.builders = builders;
        this.renderData = renderData;
    }

    @Override
    public ModelVertexSink getSink(ModelQuadFacing facing) {
        return this.builders[facing.ordinal()];
    }

    @Override
    public ChunkRenderData.Builder getRenderData() {
        return this.renderData;
    }
}
