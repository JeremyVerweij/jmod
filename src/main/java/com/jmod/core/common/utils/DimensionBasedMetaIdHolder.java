package com.jmod.core.common.utils;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;

import static com.jmod.core.common.utils.CordPackingUtils.*;

public class DimensionBasedMetaIdHolder {
    private final Long2ObjectMap<Int2IntMap> chunkToPosToIdMap;

    public DimensionBasedMetaIdHolder() {
        this.chunkToPosToIdMap = new Long2ObjectOpenHashMap<>();
    }

    public Int2IntMap getPosToIdMapOrCreateIfNotExist(long chunkPos) {
        if (!chunkToPosToIdMap.containsKey(chunkPos))
            chunkToPosToIdMap.put(chunkPos, new Int2IntOpenHashMap());

        return chunkToPosToIdMap.get(chunkPos);
    }

    public void putChunkIdMap(int chunkX, int chunkZ, Int2IntMap map){
        this.chunkToPosToIdMap.put(ChunkPos.asLong(chunkX, chunkZ), map);
    }

    public Int2IntMap getPosToIdMapOrCreateIfNotExist(ChunkPos chunkPos) {
        return getPosToIdMapOrCreateIfNotExist(getLongFromChunkPos(chunkPos));
    }

    public void putIdInPlace(int x, int y, int z, int id) {
        getPosToIdMapOrCreateIfNotExist(blockPosToChunkLong(x, z)).put(getPosInChunk(x, y, z), id);
    }

    public int getIdFromPlace(int x, int y, int z) {
        Int2IntMap map = getPosToIdMapOrCreateIfNotExist(blockPosToChunkLong(x, z));
        int key = getPosInChunk(x, y, z);

        if (!map.containsKey(key)) return 0;

        return map.get(key);
    }

    public void remove(int x, int y, int z) {
        getPosToIdMapOrCreateIfNotExist(blockPosToChunkLong(x, z)).remove(getPosInChunk(x, y, z));
    }

    public void invalidateChunk(int chunkX, int chunkY) {
        this.chunkToPosToIdMap.remove(ChunkPos.asLong(chunkX, chunkY));
    }

    public Int2IntMap getChunkMap(int chunkX, int chunkZ) {
        return this.chunkToPosToIdMap.get(ChunkPos.asLong(chunkX, chunkZ));
    }

    public Long2ObjectMap<Int2IntMap> getMap() {
        return this.chunkToPosToIdMap;
    }
}
