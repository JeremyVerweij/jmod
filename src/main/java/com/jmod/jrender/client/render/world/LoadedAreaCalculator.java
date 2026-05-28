package com.jmod.jrender.client.render.world;

import com.jmod.jrender.client.render.opengl.vao.AttributePointersBuilder;
import com.jmod.jrender.client.render.world.chunk.JRenderChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.*;

import static com.jmod.jrender.client.debug.ExtendedDebug.DISABLE_CHUNK_FRUSTUM_CAMERA_MODE;

public class LoadedAreaCalculator {
    private final JRenderGlobal renderGlobal;
    private final AttributePointersBuilder attributes;
    private final Map<ChunkPos, JRenderChunk> renderChunks;
    private final List<JRenderChunk> unUsedRenderChunks;
    private final Set<JRenderChunk> chunksToBuild;
    private int renderDistance;
    private int playerChunkX, playerChunkZ;
    private boolean everUpdated = false;

    public LoadedAreaCalculator(JRenderGlobal renderGlobal, Set<JRenderChunk> chunksToBuild, AttributePointersBuilder attributes){
        this.renderGlobal = renderGlobal;
        this.chunksToBuild = chunksToBuild;
        this.attributes = attributes;

        this.renderChunks = new HashMap<>();
        this.unUsedRenderChunks = new ArrayList<>();
        this.renderDistance = 0;
        this.playerChunkX = 0;
        this.playerChunkZ = 0;
    }

    public void updatePosition(int chunkX, int chunkZ){
        if (DISABLE_CHUNK_FRUSTUM_CAMERA_MODE.isEnabled()) return;
        if (chunkX == this.playerChunkX && chunkZ == this.playerChunkZ && this.everUpdated) return;
        this.everUpdated = true;

        this.playerChunkX = chunkX;
        this.playerChunkZ = chunkZ;

        this.calculatePositions();
    }

    public void setRenderDistance(int renderDistance, boolean update){
        if (renderDistance == this.renderDistance) return;

        this.renderDistance = renderDistance;
        int renderDistanceCalculated = (this.renderDistance << 1) + 1;  //renderDistance * 2 + 1
        int size = renderDistanceCalculated * renderDistanceCalculated;

        int toAdd = size - this.renderChunks.size();
        int toRemove = this.renderChunks.size() - size;

        System.out.println(toAdd + "_" + toRemove);

        if (toAdd > 0){
            for (int i = 0; i < toAdd; i++) {
                this.unUsedRenderChunks.add(new JRenderChunk(this.renderGlobal, this.attributes));
            }
        }

        if (toRemove > 0){
            int removed = 0;
            for (ChunkPos pos : this.renderChunks.keySet()) {
                if (removed >= toRemove) break;

                this.renderChunks.remove(pos);

                removed++;
            }
        }


        if (update)
            this.calculatePositions();
    }

    public void setRenderDistance(int renderDistance){
        this.setRenderDistance(renderDistance, false);
    }

    private void calculatePositions(){
        int renderDistanceCalculated = (this.renderDistance << 1) + 1;  //renderDistance * 2 + 1
        int startX = this.playerChunkX - this.renderDistance;
        int startZ = this.playerChunkZ - this.renderDistance;
        int endX = this.playerChunkX + this.renderDistance;
        int endZ = this.playerChunkZ + this.renderDistance;

        List<JRenderChunk> renderChunksNeedPositionUpdate = new ArrayList<>(this.unUsedRenderChunks);
        this.unUsedRenderChunks.clear();

        for (Iterator<Map.Entry<ChunkPos, JRenderChunk>> renderChunkIterator = this.renderChunks.entrySet().iterator(); renderChunkIterator.hasNext();){
            ChunkPos position = renderChunkIterator.next().getKey();

            int posX = position.x;
            int posZ = position.z;

            if (posX >= startX && posX <= endX && posZ >= startZ && posZ <= endZ) {
                continue; //you are fine
            }

            JRenderChunk renderChunk = this.renderChunks.get(position);
            renderChunksNeedPositionUpdate.add(renderChunk);

            renderChunkIterator.remove();
        }

        this.chunksToBuild.addAll(renderChunksNeedPositionUpdate);

        for (int xOffset = 0; xOffset < renderDistanceCalculated; xOffset++) {
            for (int zOffset = 0; zOffset < renderDistanceCalculated; zOffset++) {
                if (renderChunksNeedPositionUpdate.isEmpty()) break;

                int posX = startX + xOffset;
                int posZ = startZ + zOffset;
                ChunkPos position = new ChunkPos(posX, posZ);

                if (!this.renderChunks.containsKey(position)){
                    JRenderChunk renderChunk = renderChunksNeedPositionUpdate.getLast();

                    renderChunk.setPos(posX, posZ);
                    this.renderChunks.put(position, renderChunk);

                    renderChunksNeedPositionUpdate.removeLast();
                }
            }
        }
    }

    public Collection<JRenderChunk> getRenderChunks() {
        return this.renderChunks.values();
    }
}
