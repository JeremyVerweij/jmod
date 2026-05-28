package com.jmod.jrender.client.render.world.chunk.data;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkDataNonEmpty {
    private final List<ChunkDataEntry> data;

    public ChunkDataNonEmpty(Chunk chunk){
        this.data = new ArrayList<>();
        this.loadData(chunk);
    }

    @SuppressWarnings("deprecation")
    private void loadData(Chunk chunk){
        ChunkPos pos = chunk.getPos();

        for (int x = pos.getXStart(); x <= pos.getXEnd(); x++) {
            for (int z = pos.getZStart(); z <= pos.getZEnd(); z++) {
                for (int y = 0; y < 256; y++) {
                    IBlockState state = chunk.getBlockState(x, y, z);

                    if (state.getBlock() == Blocks.AIR) continue;

                    //Check if all surrounding blocks are non-air
                    if (x != pos.getXStart() && x != pos.getXEnd() &&
                        y != 0 && y != 255 &
                        z != pos.getZStart() && z != pos.getZEnd()){

                        boolean neighbourAir = false;

                        IBlockState nState;

                        for (int i = -1; i <= 1; i++) {
                            if((nState = chunk.getBlockState(x + i, y, z)).getBlock() == Blocks.AIR ||
                                    !nState.getBlock().isFullCube(nState)){
                                neighbourAir = true;
                                break;
                            }

                            if((nState = chunk.getBlockState(x, y, z + i)).getBlock() == Blocks.AIR ||
                                    !nState.getBlock().isFullCube(nState)){
                                neighbourAir = true;
                                break;
                            }

                            if((nState = chunk.getBlockState(x, y + i, z)).getBlock() == Blocks.AIR ||
                                    !nState.getBlock().isFullCube(nState)){
                                neighbourAir = true;
                                break;
                            }
                        }

                        if (!neighbourAir) continue;
                    }

                    IBlockState newState = new BlockStateContainer.StateImplementation(state.getBlock(),
                            ImmutableMap.copyOf(state.getProperties()));

                    this.data.add(new ChunkDataEntry(x, y, z, newState));
                }
            }
        }
    }

    public List<ChunkDataEntry> getData() {
        return data;
    }

    public record ChunkDataEntry(int x, int y, int z, IBlockState state){}
}
