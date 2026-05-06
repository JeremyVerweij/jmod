package com.jmod.jrender.client.model.vertex.type;

import com.jmod.jrender.client.render.chunk.format.ChunkMeshAttribute;
import com.jmod.jrender.client.render.chunk.format.ModelVertexSink;

public interface ChunkVertexType extends BlittableVertexType<ModelVertexSink>, CustomVertexType<ModelVertexSink, ChunkMeshAttribute> {
    /**
     * @return The scale to be applied to vertex coordinates
     */
    float getModelScale();

    /**
     * @return The scale to be applied to texture coordinates
     */
    float getTextureScale();
}
