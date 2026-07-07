package com.jmod.core.common.net;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MetaIdsChunkPacket implements IMessage {
    private final Int2IntMap idMap;
    private int chunkX, chunkZ;

    public MetaIdsChunkPacket(){
        this.idMap = new Int2IntOpenHashMap();
    }

    public MetaIdsChunkPacket(ChunkPos pos, Int2IntMap idMap){
        this.idMap = idMap;
        this.chunkX = pos.x;
        this.chunkZ = pos.z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();

        while (buf.readableBytes() > 0) {
            int key = buf.readInt();
            int id = buf.readInt();
            this.idMap.put(key, id);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);

        for (Integer key : idMap.keySet()) {
            int id = idMap.get(key);
            buf.writeInt(key);
            buf.writeInt(id);
        }
    }

    public Int2IntMap getIdMap() {
        return idMap;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }
}
