package com.jmod.jrender.client.render.world.chunk;

import com.jmod.jrender.client.render.world.ChunkViewFrustum;
import com.jmod.jrender.client.render.world.JRenderGlobal;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ChunkRenderer {
    private final ThreadPoolExecutor threadPool;
    private final JRenderGlobal renderGlobal;
    private final Set<JRenderChunk.JChunkRenderBuildTask> chunksBuilding;
    private final ChunkViewFrustum chunkViewFrustum;

    public ChunkRenderer(JRenderGlobal renderGlobal){
        this.renderGlobal = renderGlobal;
        this.chunkViewFrustum = new ChunkViewFrustum(Minecraft.getMinecraft().gameSettings.renderDistanceChunks, this.renderGlobal);

        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);

        this.chunksBuilding = new HashSet<>();
    }

    public void setUseThreads(int amount){
        this.threadPool.setCorePoolSize(amount);
        this.threadPool.setMaximumPoolSize(amount);
    }

    public void draw(){
        for (JRenderChunk renderChunk : this.chunkViewFrustum.getRenderChunks()) {
            renderChunk.draw();
        }
    }

    public void setupTerrain(Entity viewEntity){
        chunkViewFrustum.updatePos(viewEntity.chunkCoordX, viewEntity.chunkCoordZ);
    }

    public void updateChunk(int minX, int minZ, int maxX, int maxZ){
        int chunkMinX = MathHelper.intFloorDiv(minX, 16);
        int chunkMinZ = MathHelper.intFloorDiv(minZ, 16);
        int chunkMaxX = MathHelper.intFloorDiv(maxX, 16);
        int chunkMaxZ = MathHelper.intFloorDiv(maxZ, 16);

        for (int x = chunkMinX; x < chunkMaxX; x++) {
            for (int z = chunkMinZ; z < chunkMaxZ; z++) {
                JRenderChunk renderChunk = this.chunkViewFrustum.getChunk(x, z);
                if (renderChunk == null) continue;

                if (renderChunk.task != null){
                    this.chunksBuilding.remove(renderChunk.task);
                    renderChunk.task.stop();
                }

                JRenderChunk.JChunkRenderBuildTask task = new JRenderChunk.JChunkRenderBuildTask(renderChunk);
                renderChunk.loadData();
                this.chunksBuilding.add(task);
                renderChunk.task = task;

                this.threadPool.submit(task);
            }
        }
    }

    public void updateChunks(){
        for (Iterator<JRenderChunk.JChunkRenderBuildTask> chunkIterator = this.chunksBuilding.iterator(); chunkIterator.hasNext();){
            JRenderChunk.JChunkRenderBuildTask renderBuildTask = chunkIterator.next();

            if (renderBuildTask.isStopped()){
                chunkIterator.remove();
                renderBuildTask.renderChunk.task = null;
            }
        }

        for (Iterator<JRenderChunk.JChunkRenderBuildTask> chunkIterator = this.chunksBuilding.iterator(); chunkIterator.hasNext();){
            JRenderChunk.JChunkRenderBuildTask renderBuildTask = chunkIterator.next();
            if (renderBuildTask.isBuilt()){
                renderBuildTask.renderChunk.upload();
                chunkIterator.remove();
                renderBuildTask.renderChunk.task = null;
            }
        }
    }

    public void setRenderDistance(int renderDistanceChunks) {
        this.chunkViewFrustum.setRenderDistance(renderDistanceChunks);
    }
}
