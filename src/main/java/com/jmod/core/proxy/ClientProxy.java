package com.jmod.core.proxy;

import com.jmod.core.client.ClientMetaIdHolder;
import com.jmod.core.client.model.MetaBlockModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy{
    public ClientMetaIdHolder clientMetaIdHolder;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        this.clientMetaIdHolder = new ClientMetaIdHolder();
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        ModelResourceLocation loc = new ModelResourceLocation(this.testBlock.getRegistryName().toString(),
                "normal");

        IBakedModel object = event.getModelRegistry().getObject(loc);

        ModelResourceLocation loc2 = new ModelResourceLocation(this.testBlock.getRegistryName().toString(),
                "not_normal");

        IBakedModel object2 = event.getModelRegistry().getObject(loc2);

        if (object == null || object2 == null){
            System.out.println("ERROR: MODEL IS NULL");
            System.exit(0);
        }

        MetaBlockModel customModel = new MetaBlockModel(object, object2);
        event.getModelRegistry().putObject(loc, customModel);
        event.getModelRegistry().putObject(loc2, customModel);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event){
        ModelLoader.setCustomModelResourceLocation(testBlockItem, 0, new ModelResourceLocation("jmod:test", "inventory"));

        ModelBakery.registerItemVariants(testBlockItem,
                new ModelResourceLocation("jmod:test", "normal"),
                new ModelResourceLocation("jmod:test", "not_normal")
        );
    }

    @Override
    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        super.onChunkUnload(event);

        this.clientMetaIdHolder.invalidateChunk(event.getChunk().x, event.getChunk().z, event.getWorld().provider.getDimension());
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.clientMetaIdHolder.invalidate(event.getWorld().provider.getDimension());
    }
}
