package com.jmod.core.proxy;

import com.jmod.core.client.ClientMetaIdHolder;
import com.jmod.core.common.block.MetaBlock;
import com.jmod.core.common.block.material.PipeTestBlock;
import com.jmod.core.common.event.MetaBlockRegisterEvent;
import com.jmod.core.common.event.MetaItemRegisterEvent;
import com.jmod.core.common.event.material.MaterialRegistryEvent;
import com.jmod.core.common.item.MetaItem;
import com.jmod.core.common.item.WrenchItem;
import com.jmod.core.common.material.MaterialBuilder;
import com.jmod.core.common.material.MaterialRegistry;
import com.jmod.core.common.net.MetaIdsChunkPacket;
import com.jmod.core.common.net.MetaIdsDeltaAddPacket;
import com.jmod.core.common.net.MetaIdsDeltaDeletePacket;
import com.jmod.core.common.net.NetworkHandler;
import com.jmod.core.common.utils.MiningTier;
import com.jmod.core.server.ServerMetaIdHolder;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    private MaterialRegistry materialRegistry;
    private ServerMetaIdHolder serverMetaIdHolder;
    protected MetaBlockRegisterEvent metaBlockRegisterEvent;
    protected MetaItemRegisterEvent metaItemRegisterEvent;

    public void preInit(FMLPreInitializationEvent event) {
        NetworkHandler.register(ClientMetaIdHolder.MetaIdDeltaAddHandler.class, MetaIdsDeltaAddPacket.class, Side.CLIENT);
        NetworkHandler.register(ClientMetaIdHolder.MetaIdDeltaDeleteHandler.class, MetaIdsDeltaDeletePacket.class, Side.CLIENT);
        NetworkHandler.register(ClientMetaIdHolder.MetaIdChunkHandler.class, MetaIdsChunkPacket.class, Side.CLIENT);

        this.materialRegistry = new MaterialRegistry();
        MinecraftForge.EVENT_BUS.post(new MaterialRegistryEvent(this.materialRegistry));

        this.metaBlockRegisterEvent = new MetaBlockRegisterEvent();
        MinecraftForge.EVENT_BUS.post(metaBlockRegisterEvent);

        this.metaItemRegisterEvent = new MetaItemRegisterEvent();
        MinecraftForge.EVENT_BUS.post(metaItemRegisterEvent);

        this.serverMetaIdHolder = new ServerMetaIdHolder();
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}

    public void onServerStart(FMLServerStartingEvent event){
    }

    public void onServerStop(FMLServerStoppingEvent event){
        this.serverMetaIdHolder.saveAll();
        this.serverMetaIdHolder.compact();
        this.serverMetaIdHolder.invalidate();
    }

    @SubscribeEvent
    public void onMaterialRegister(MaterialRegistryEvent event){
        event.REGISTRY.register(new MaterialBuilder(0xFFFF00FF, "")
                .enableFluidPipe(1)
                .enableTools(true, true, true, 5f, 100, MiningTier.STEEL)
                .build());
        event.REGISTRY.register(new MaterialBuilder(0xFF0000FF, "")
                .enableFluidPipe(1)
                .build());
        event.REGISTRY.register(new MaterialBuilder(0xFFFF0000, "")
                .enableFluidPipe(1)
                .build());
        event.REGISTRY.register(new MaterialBuilder(0xFF00FF00, "")
                .enableFluidPipe(1)
                .build());
    }

    @SubscribeEvent
    public void onMetaBlockRegister(MetaBlockRegisterEvent event){
        event.register(new PipeTestBlock(4));
        event.register(new PipeTestBlock(6));
        event.register(new PipeTestBlock(8));
        event.register(new PipeTestBlock(10));
        event.register(new PipeTestBlock(12));
    }

    @SubscribeEvent
    public void onMetaItemRegister(MetaItemRegisterEvent event){
        event.register(new WrenchItem());
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        for (MetaBlock metaBlock : this.metaBlockRegisterEvent.getRegistry()) {
            event.getRegistry().register(metaBlock);
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        this.registerItemBlocks(event);

        for (MetaItem metaItem : this.metaItemRegisterEvent.getRegistry()) {
            event.getRegistry().register(metaItem);
        }
    }

    public void registerItemBlocks(RegistryEvent.Register<Item> event){
        for (MetaBlock metaBlock : this.metaBlockRegisterEvent.getRegistry()) {
            event.getRegistry().registerAll(metaBlock.getItemBlock());
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (!event.getWorld().isRemote){
            this.serverMetaIdHolder.invalidateChunk(event.getChunk().x, event.getChunk().z, event.getWorld().provider.getDimension());
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (!event.getWorld().isRemote){
            this.serverMetaIdHolder.loadChunk(event.getChunk().x, event.getChunk().z, event.getWorld().provider.getDimension());
        }
    }

    @SubscribeEvent
    public void onChunkWatch(ChunkWatchEvent.Watch event){
        EntityPlayerMP player = event.getPlayer();
        ChunkPos pos = event.getChunkInstance().getPos();
        World world = player.world;

        Int2IntMap idMap = this.serverMetaIdHolder.getDimensionMetaHolder(world.provider.getDimension()).getPosToIdMapOrCreateIfNotExist(pos);

        if (idMap != null && !idMap.isEmpty()){
            NetworkHandler.sendToClient(new MetaIdsChunkPacket(pos, idMap), player);
        }
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event){
        if(!event.getWorld().isRemote){
            this.serverMetaIdHolder.saveChunk(event.getChunk().x, event.getChunk().z, event.getWorld().provider.getDimension());
        }
    }

    public ServerMetaIdHolder getServerMetaIdHolder() {
        return serverMetaIdHolder;
    }

    public MaterialRegistry getMaterialRegistry() {
        return materialRegistry;
    }
}
