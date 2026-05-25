package com.jmod.jrender.client.render.world.chunk;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.Set;

public class JRenderChunk extends RenderChunk {
    public JRenderChunk(World worldIn, RenderGlobal renderGlobalIn, int indexIn) {
        super(worldIn, renderGlobalIn, indexIn);
    }

    @Override
    public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        CompiledChunk compiledchunk = new CompiledChunk();
        int i = 1;
        BlockPos blockpos = this.position;
        BlockPos blockpos1 = blockpos.add(15, 15, 15);
        generator.getLock().lock();

        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }

            generator.setCompiledChunk(compiledchunk);
        } finally {
            generator.getLock().unlock();
        }

        VisGraph visibilityGraph = new VisGraph();
        Set<TileEntity> tileEntitySet = Sets.newHashSet();
        if (!this.worldView.isEmpty()) {
            ++renderChunksUpdated;
            boolean[] layersUsed = new boolean[BlockRenderLayer.values().length];
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

            for(BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos1)) {
                IBlockState iblockstate = this.worldView.getBlockState(blockpos$mutableblockpos);
                Block block = iblockstate.getBlock();
                if (iblockstate.isOpaqueCube()) {
                    visibilityGraph.setOpaqueCube(blockpos$mutableblockpos);
                }

                if (block.hasTileEntity(iblockstate)) {
                    //DISABLED TILE ENTITIES FOR NOW, MIGH BE REMOVED FOREVER AND EVER
//                    TileEntity tileentity = this.worldView.getTileEntity(blockpos$mutableblockpos, Chunk.EnumCreateEntityType.CHECK);
//                    if (tileentity != null) {
//                        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getRenderer(tileentity);
//                        if (tileentityspecialrenderer != null) {
//                            if (tileentityspecialrenderer.isGlobalRenderer(tileentity)) {
//                                tileEntitySet.add(tileentity);
//                            } else {
//                                compiledchunk.addTileEntity(tileentity);
//                            }
//                        }
//                    }
                }

                for(BlockRenderLayer blockrenderlayer1 : BlockRenderLayer.values()) {
                    if (block.canRenderInLayer(iblockstate, blockrenderlayer1)) {
                        ForgeHooksClient.setRenderLayer(blockrenderlayer1);
                        int j = blockrenderlayer1.ordinal();
                        if (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE) {
                            BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);
                            if (!compiledchunk.isLayerStarted(blockrenderlayer1)) {
                                compiledchunk.setLayerStarted(blockrenderlayer1);
                                this.preRenderBlocks(bufferbuilder, blockpos);
                            }

                            layersUsed[j] |= blockrendererdispatcher.renderBlock(iblockstate, blockpos$mutableblockpos, this.worldView, bufferbuilder);
                        }
                    }
                }

                ForgeHooksClient.setRenderLayer(null);
            }

            for(BlockRenderLayer blockrenderlayer : BlockRenderLayer.values()) {
                if (layersUsed[blockrenderlayer.ordinal()]) {
                    compiledchunk.setLayerUsed(blockrenderlayer);
                }

                if (compiledchunk.isLayerStarted(blockrenderlayer)) {
                    this.postRenderBlocks(blockrenderlayer, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), compiledchunk);
                }
            }
        }

        compiledchunk.setVisibility(visibilityGraph.computeVisibility());
        this.lockCompileTask.lock();

        try {
            Set<TileEntity> tileEntitiesUsedNew = Sets.newHashSet(tileEntitySet);
            Set<TileEntity> tileEntitiesUsedOld = Sets.newHashSet(this.setTileEntities);
            tileEntitiesUsedNew.removeAll(this.setTileEntities);
            tileEntitiesUsedOld.removeAll(tileEntitySet);
            this.setTileEntities.clear();
            this.setTileEntities.addAll(tileEntitySet);
            this.renderGlobal.updateTileEntities(tileEntitiesUsedOld, tileEntitiesUsedNew);
        } finally {
            this.lockCompileTask.unlock();
        }
    }
}
