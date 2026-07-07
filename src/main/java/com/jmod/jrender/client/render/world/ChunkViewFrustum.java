package com.jmod.jrender.client.render.world;

import com.jmod.jrender.client.render.opengl.vao.AttributePointersBuilder;
import com.jmod.jrender.client.render.opengl.vao.VertexType;
import com.jmod.jrender.client.render.world.chunk.JRenderChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChunkViewFrustum {
    private final JRenderGlobal renderGlobal;

    private int chunkPosX;
    private int chunkPosZ;
    private int renderDistance;
    private JRenderChunk[] renderChunks;

    public ChunkViewFrustum(int renderDistance, JRenderGlobal renderGlobal){
        this.renderGlobal = renderGlobal;
        this.renderDistance = renderDistance;

        this.renderChunks = null;
    }

    public int getChunkAmountPerAxis(){
        return (this.renderDistance << 1) + 1; // renderDistance * 2 + 1
    }

    public void updatePos(int chunkPosX, int chunkPosZ){
        if (chunkPosX != this.chunkPosX || chunkPosZ != this.chunkPosZ || this.renderChunks == null){
            this.chunkPosX = chunkPosX;
            this.chunkPosZ = chunkPosZ;

            if(this.renderChunks == null){
                this.renderChunks = new JRenderChunk[getChunkAmountPerAxis() * getChunkAmountPerAxis()];

                for (int i = 0; i < this.renderChunks.length; i++) {
                    this.renderChunks[i] = new JRenderChunk(this.renderGlobal, new AttributePointersBuilder()
                            .addAttribute(VertexType.FLOAT, 3, false)
                            .addAttribute(VertexType.UNSIGNED_BYTE, 4, true)
                            .addAttribute(VertexType.SHORT, 2, false));

                    this.renderChunks[i].setPos((i % getChunkAmountPerAxis()) + this.chunkPosX - this.renderDistance,
                            i / getChunkAmountPerAxis() + this.chunkPosZ - this.renderDistance);
                }
                return;
            }

            Long2ObjectMap<JRenderChunk> inBounds = new Long2ObjectOpenHashMap<>();
            List<JRenderChunk> outOfBounds = new LinkedList<>();

            for (JRenderChunk renderChunk : this.renderChunks) {
                int x = renderChunk.getPosition().getX();
                int z = renderChunk.getPosition().getZ();

                if (x < this.chunkPosX - this.renderDistance || x > this.chunkPosX + this.renderDistance ||
                z < this.chunkPosZ - this.renderDistance || z > this.chunkPosZ + this.renderDistance){
                    outOfBounds.add(renderChunk);
                }else{
                    inBounds.put(ChunkPos.asLong(x, z), renderChunk);
                }
            }

            for (int i = 0; i < this.renderChunks.length; i++) {
                int x = (i % getChunkAmountPerAxis()) + this.chunkPosX - this.renderDistance;
                int z = (i / getChunkAmountPerAxis()) + this.chunkPosZ - this.renderDistance;

                if (inBounds.containsKey(ChunkPos.asLong(x, z))){
                    this.renderChunks[i] = inBounds.get(ChunkPos.asLong(x, z));
                }else{
                    JRenderChunk renderChunk = outOfBounds.removeFirst();
                    renderChunk.reset();
                    renderChunk.setPos(x, z);
                    this.renderChunks[i] = renderChunk;
                }
            }
        }
    }

    public void setRenderDistance(int renderDistance){
        this.renderDistance = renderDistance;

        if (this.renderChunks != null){
            for (JRenderChunk renderChunk : this.renderChunks) {
                renderChunk.delete();
            }
        }

        this.renderChunks = null;
    }

    public JRenderChunk[] getRenderChunks() {
        return renderChunks;
    }

    public JRenderChunk getChunk(int x, int z) {
        int xO = x - this.chunkPosX + this.renderDistance;
        int zO = z - this.chunkPosZ + this.renderDistance;
        int index = (zO * getChunkAmountPerAxis()) + xO;

        if (index < 0 || index >= this.renderChunks.length){
            return null;
        }else{
            return this.renderChunks[index];
        }
    }
}
