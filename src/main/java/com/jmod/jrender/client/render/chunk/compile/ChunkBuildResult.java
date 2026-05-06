package com.jmod.jrender.client.render.chunk.compile;

import com.jmod.jrender.client.render.chunk.ChunkGraphicsState;
import com.jmod.jrender.client.render.chunk.ChunkRenderContainer;
import com.jmod.jrender.client.render.chunk.data.ChunkRenderData;
import com.jmod.jrender.client.render.chunk.passes.BlockRenderPass;

/**
 * The result of a chunk rebuild task which contains any and all data that needs to be processed or uploaded on
 * the main thread. If a task is cancelled after finishing its work and not before the result is processed, the result
 * will instead be discarded.
 */
public class ChunkBuildResult<T extends ChunkGraphicsState> {
    public final ChunkRenderContainer<T> render;
    public final ChunkRenderData data;
    public BlockRenderPass[] passesToUpload;

    public ChunkBuildResult(ChunkRenderContainer<T> render, ChunkRenderData data) {
        this.render = render;
        this.data = data;
        this.passesToUpload = BlockRenderPass.VALUES;
    }
}
