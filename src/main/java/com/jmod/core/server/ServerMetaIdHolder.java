package com.jmod.core.server;

import com.jmod.core.common.utils.DimensionBasedMetaIdHolder;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static com.jmod.core.common.utils.RegionFileManager.*;

public class ServerMetaIdHolder {
    private final Int2ObjectMap<DimensionBasedMetaIdHolder> map;
    private final Set<String> regionsNeedCompacting;

    public ServerMetaIdHolder(){
        this.map = new Int2ObjectArrayMap<>();
        this.regionsNeedCompacting = new HashSet<>();
    }

    public DimensionBasedMetaIdHolder getDimensionMetaHolder(int dimension){
        if (!this.map.containsKey(dimension))
            this.map.put(dimension, new DimensionBasedMetaIdHolder());

        return this.map.get(dimension);
    }

    public int getId(int x, int y, int z, int dimension){
        return getDimensionMetaHolder(dimension).getIdFromPlace(x, y, z);
    }

    public void putId(int x, int y, int z, int dimension, int id){
        getDimensionMetaHolder(dimension).putIdInPlace(x, y, z, id);
    }

    public void invalidateBlock(int x, int y, int z, int dimension){
        getDimensionMetaHolder(dimension).remove(x, y, z);
    }

    public void saveChunk(int chunkX, int chunkZ, int dimension){
        //add saving
        Int2IntMap map = getDimensionMetaHolder(dimension).getChunkMap(chunkX, chunkZ);

        boolean needCompacting = false;

        if (map != null && !map.isEmpty()){
            int size = map.size();
            ByteBuffer buffer = ByteBuffer.allocate(size * 8);

            for (int key : map.keySet()) {
                int id = map.get(key);
                buffer.putInt(key);
                buffer.putInt(id);
            }

            buffer.rewind();

            needCompacting = writeChunk(chunkX, chunkZ, dimension, buffer);
        }else{
            needCompacting = removeChunk(chunkX, chunkZ, dimension);
        }

        if (needCompacting){
            this.regionsNeedCompacting.add(getRegionFile(chunkX, chunkZ, dimension).toString());
        }
    }

    public void invalidateChunk(int chunkX, int chunkZ, int dimension){
        getDimensionMetaHolder(dimension).invalidateChunk(chunkX, chunkZ);
    }

    public void loadChunk(int chunkX, int chunkZ, int dimension){
        ByteBuffer buffer = readChunk(chunkX, chunkZ, dimension);

        if (buffer != null){
            int size = buffer.capacity() >> 3;
            Int2IntMap map = new Int2IntOpenHashMap(size);

            for (int i = 0; i < size; i++) {
                int key = buffer.getInt();
                int id = buffer.getInt();

                map.put(key, id);
            }

            getDimensionMetaHolder(dimension).putChunkIdMap(chunkX, chunkZ, map);
        }
    }

    public void invalidate(){
        this.map.clear();
        this.regionsNeedCompacting.clear();
    }

    public void saveAll(){
        for (int dimension : this.map.keySet()) {
            DimensionBasedMetaIdHolder dimensionBasedMetaIdHolder = this.map.get(dimension);
            for (long packedPos : dimensionBasedMetaIdHolder.getMap().keySet()) {
                int cx = (int) (packedPos & 0xFFFFFFFFL);
                int cz = (int) (packedPos >>> 32);

                saveChunk(cx, cz, dimension);
            }
        }
    }

    public void compact(){
        for (String region : this.regionsNeedCompacting) {
            try {
                compactRegion(Paths.get(region));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
