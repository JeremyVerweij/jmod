package com.jmod.jrender.client.render.world.chunk;

import com.jmod.jrender.client.render.opengl.vao.AttributePointersBuilder;
import com.jmod.jrender.client.render.opengl.vao.VertexType;
import com.jmod.jrender.client.render.world.JRenderGlobal;
import com.jmod.jrender.client.render.world.LoadedAreaCalculator;
import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ChunkRenderer {
    private final ThreadPoolExecutor threadPool;
    private final JRenderGlobal renderGlobal;
    private final Set<JRenderChunk> chunksToBuild;
    private final Set<JRenderChunk> chunksBuilding;
    private final LoadedAreaCalculator loadedAreaCalculator;

    public ChunkRenderer(JRenderGlobal renderGlobal){
        this.renderGlobal = renderGlobal;

        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);

        this.chunksToBuild = new HashSet<>();
        this.chunksBuilding = new HashSet<>();

        this.loadedAreaCalculator = new LoadedAreaCalculator(this.renderGlobal, this.chunksToBuild, new AttributePointersBuilder()
                .addAttribute(VertexType.FLOAT, 3, false)
                .addAttribute(VertexType.UNSIGNED_BYTE, 4, true)
                .addAttribute(VertexType.SHORT, 2, false));

        this.loadedAreaCalculator.setRenderDistance(4, false);
    }

    public void setUseThreads(int amount){
        this.threadPool.setCorePoolSize(amount);
        this.threadPool.setMaximumPoolSize(amount);
    }

    public void draw(){
        for (JRenderChunk renderChunk : this.loadedAreaCalculator.getRenderChunks()) {
            renderChunk.draw();
        }
    }

    public void setupTerrain(Entity viewEntity){
        this.loadedAreaCalculator.updatePosition(viewEntity.chunkCoordX, viewEntity.chunkCoordZ);

        for (Iterator<JRenderChunk> chunkIterator = this.chunksToBuild.iterator(); chunkIterator.hasNext();){
            JRenderChunk renderChunk = chunkIterator.next();

            if (this.renderGlobal.getWorld().getChunk(renderChunk.getPosition()).isPopulated()){
                this.chunksBuilding.remove(renderChunk);
                renderChunk.stop();
                renderChunk.loadData();
                this.chunksBuilding.add(renderChunk);

                this.threadPool.submit(renderChunk);

                chunkIterator.remove();
            }
        }

        for (Iterator<JRenderChunk> chunkIterator = this.chunksBuilding.iterator(); chunkIterator.hasNext();){
            JRenderChunk renderChunk = chunkIterator.next();
            if (renderChunk.isBuilt()){
                renderChunk.upload();
                chunkIterator.remove();
            }
        }
    }
}
