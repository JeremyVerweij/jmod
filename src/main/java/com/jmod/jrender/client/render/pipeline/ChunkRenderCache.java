package com.jmod.jrender.client.render.pipeline;

import com.jmod.jrender.client.model.quad.blender.BiomeColorBlender;
import net.minecraft.client.Minecraft;

public class ChunkRenderCache {
    protected BiomeColorBlender createBiomeColorBlender() {
    	 return BiomeColorBlender.create(Minecraft.getMinecraft());
    }
}
