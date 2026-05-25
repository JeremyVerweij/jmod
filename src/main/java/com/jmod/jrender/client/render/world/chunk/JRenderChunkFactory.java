package com.jmod.jrender.client.render.world.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.World;

public class JRenderChunkFactory implements IRenderChunkFactory {
    public JRenderChunkFactory() {
    }

    public RenderChunk create(World worldIn, RenderGlobal renderGlobalIn, int index) {
        return new JRenderChunk(worldIn, renderGlobalIn, index);
    }
}
