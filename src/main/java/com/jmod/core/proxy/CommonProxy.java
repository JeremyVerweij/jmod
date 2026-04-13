package com.jmod.core.proxy;

import com.jmod.JMod;
import com.jmod.core.client.ClientMetaIdHolder;
import com.jmod.core.common.block.MetaBlock;
import com.jmod.core.common.net.MetaIdsChunkPacket;
import com.jmod.core.common.net.MetaIdsDeltaAddPacket;
import com.jmod.core.common.net.MetaIdsDeltaDeletePacket;
import com.jmod.core.common.net.NetworkHandler;
import com.jmod.core.server.ServerMetaIdHolder;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    private ServerMetaIdHolder serverMetaIdHolder;
    protected MetaBlock testBlock;
    protected Item testBlockItem;

    public void preInit(FMLPreInitializationEvent event) {
        NetworkHandler.register(ClientMetaIdHolder.MetaIdDeltaAddHandler.class, MetaIdsDeltaAddPacket.class, Side.CLIENT);
        NetworkHandler.register(ClientMetaIdHolder.MetaIdDeltaDeleteHandler.class, MetaIdsDeltaDeletePacket.class, Side.CLIENT);
        NetworkHandler.register(ClientMetaIdHolder.MetaIdChunkHandler.class, MetaIdsChunkPacket.class, Side.CLIENT);

        this.testBlock = new MetaBlock(JMod.MODID, "test", Material.ANVIL, CreativeTabs.MISC, (short) 10);
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
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        System.out.println("REGISTER BLOCK");
        event.getRegistry().register(testBlock);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        System.out.println("REGISTER ITEM");
        this.registerItemBlocks(event);
    }

    public void registerItemBlocks(RegistryEvent.Register<Item> event){
        event.getRegistry().register(testBlock.getItemBlock());
        this.testBlockItem = testBlock.getItemBlock();
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
}
