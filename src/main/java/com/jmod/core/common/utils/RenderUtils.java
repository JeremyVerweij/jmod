package com.jmod.core.common.utils;

import net.minecraft.client.Minecraft;

public class RenderUtils {
    public static void redrawChunk(int chunkX, int chunkZ) {
        int x1 = chunkX << 4; // Same as chunkX * 16
        int z1 = chunkZ << 4; // Same as chunkZ * 16
        int x2 = x1 + 15;
        int z2 = z1 + 15;

        Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(x1, 0, z1, x2, 255, z2);
    }
}
