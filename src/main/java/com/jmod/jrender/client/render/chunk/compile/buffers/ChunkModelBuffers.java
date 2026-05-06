package com.jmod.jrender.client.render.chunk.compile.buffers;

import com.jmod.jrender.client.model.quad.properties.ModelQuadFacing;
import com.jmod.jrender.client.render.chunk.data.ChunkRenderData;
import com.jmod.jrender.client.render.chunk.format.ModelVertexSink;

public interface ChunkModelBuffers {
    ModelVertexSink getSink(ModelQuadFacing facing);

    @Deprecated
    ChunkRenderData.Builder getRenderData();
}
