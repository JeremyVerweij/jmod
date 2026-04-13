package com.jmod.core.common.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class CordPackingUtils {
    public static long getLongFromChunkPos(ChunkPos pos){
        return ChunkPos.asLong(pos.x, pos.z);
    }

    public static int getPosInChunk(int x, int y, int z){
        return (y << 8) | ((x & 15) << 4) | (z & 15);
    }

    public static int getPosInChunk(BlockPos pos){
        return getPosInChunk(pos.getX(), pos.getY(), pos.getZ());
    }

    public static long blockPosToChunkLong(int x, int z){
        return ChunkPos.asLong(x >> 4, z >> 4);
    }
}
