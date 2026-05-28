package com.jmod.jrender.client.render.world.chunk.data;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;

public class RawChunkRenderData {
    private final IBlockState[] data;

    public RawChunkRenderData(){
        this.data = new IBlockState[16 * 256 * 16];
    }

    public void loadData(Chunk chunk){
        ChunkPos pos = chunk.getPos();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int yPos = 0; yPos < 256; yPos++) {
                    int index = getIndex(x, yPos, z);
                    int xPos = x + pos.getXStart();
                    int zPos = z + pos.getZStart();

                    IBlockState state = chunk.getBlockState(xPos, yPos, zPos);

                    IBlockState newState = new BlockStateContainer.StateImplementation(state.getBlock(),
                            ImmutableMap.copyOf(state.getProperties()));

                    this.data[index] = newState;
                }
            }
        }
    }

    public int getIndex(int x, int y, int z){
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16) return -1;

        return ((y & 255) << 8) | ((x & 15) << 4) | (z & 15);
    }

    public int getX(int index){
        return (index >> 4) & 15;
    }

    public int getZ(int index){
        return index & 15;
    }

    public int getY(int index){
        return (index >> 8) & 255;
    }

    public @Nullable IBlockState getBlockData(int x, int y, int z){
        int index = getIndex(x, y, z);

        if (index < 0) return null;

        return this.data[index];
    }

    public IBlockState[] getData() {
        return this.data;
    }
}
