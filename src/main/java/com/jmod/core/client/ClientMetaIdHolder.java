package com.jmod.core.client;

import com.jmod.JMod;
import com.jmod.core.common.net.MetaIdsChunkPacket;
import com.jmod.core.common.net.MetaIdsDeltaAddPacket;
import com.jmod.core.common.net.MetaIdsDeltaDeletePacket;
import com.jmod.core.common.utils.DimensionBasedMetaIdHolder;
import com.jmod.core.proxy.ClientProxy;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.jmod.core.client.utils.RenderUtils.redrawChunk;

public class ClientMetaIdHolder{
    private final Int2ObjectMap<DimensionBasedMetaIdHolder> map;

    public ClientMetaIdHolder(){
        this.map = new Int2ObjectArrayMap<>();
    }

    public DimensionBasedMetaIdHolder getDimensionMetaHolder(int dimension){
        if (!this.map.containsKey(dimension))
            this.map.put(dimension, new DimensionBasedMetaIdHolder());

        return this.map.get(dimension);
    }

    public int getId(int x, int y, int z, int dimension){
        return getDimensionMetaHolder(dimension).getIdFromPlace(x, y, z);
    }

    public int getId(BlockPos pos, int dimension){
        return this.getId(pos.getX(), pos.getY(), pos.getZ(), dimension);
    }


    public void putId(int x, int y, int z, int dimension, int id){
        getDimensionMetaHolder(dimension).putIdInPlace(x, y, z, id);
    }

    public void invalidateBlock(int x, int y, int z, int dimension){
        getDimensionMetaHolder(dimension).remove(x, y, z);
    }

    public void invalidateChunk(int chunkX, int chunkY, int dimension){
        getDimensionMetaHolder(dimension).invalidateChunk(chunkX, chunkY);
    }

    public void invalidate(int dimension){
        this.map.remove(dimension);
    }

    public static class MetaIdChunkHandler implements IMessageHandler<MetaIdsChunkPacket, IMessage>{
        @Override
        public IMessage onMessage(MetaIdsChunkPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ((ClientProxy) JMod.proxy).clientMetaIdHolder.getDimensionMetaHolder(Minecraft.getMinecraft().player.dimension)
                                .putChunkIdMap(message.getChunkX(), message.getChunkZ(), message.getIdMap());

                redrawChunk(message.getChunkX(), message.getChunkZ());
            });

            return null;
        }
    }

    public static class MetaIdDeltaAddHandler implements IMessageHandler<MetaIdsDeltaAddPacket, IMessage>{
        @Override
        public IMessage onMessage(MetaIdsDeltaAddPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                boolean redraw = message.getId() != ((ClientProxy) JMod.proxy).clientMetaIdHolder.
                        getId(message.getX(), message.getY(), message.getZ(), Minecraft.getMinecraft().player.dimension);

                ((ClientProxy) JMod.proxy).clientMetaIdHolder.
                        putId(message.getX(), message.getY(), message.getZ(), Minecraft.getMinecraft().player.dimension, message.getId());

                if (redraw)
                    Minecraft.getMinecraft().world.markBlockRangeForRenderUpdate(message.getBlockPos(), message.getBlockPos());
            });

            return null;
        }
    }

    public static class MetaIdDeltaDeleteHandler implements IMessageHandler<MetaIdsDeltaDeletePacket, IMessage>{
        @Override
        public IMessage onMessage(MetaIdsDeltaDeletePacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                    ((ClientProxy) JMod.proxy).clientMetaIdHolder.
                            invalidateBlock(message.getX(), message.getY(), message.getZ(), Minecraft.getMinecraft().player.dimension);
            });
            return null;
        }
    }
}
