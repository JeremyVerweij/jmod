package com.jmod.core.proxy;

import com.jmod.core.client.ClientMetaIdHolder;
import com.jmod.core.client.model.MetaBlockModel;
import com.jmod.core.common.block.MetaBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy{
    public ClientMetaIdHolder clientMetaIdHolder;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        this.clientMetaIdHolder = new ClientMetaIdHolder();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        registerBlockColors();
        registerItemColors();
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
//        ModelResourceLocation loc = new ModelResourceLocation(this.testBlock.getRegistryName().toString(),
//                "normal");
//
//        IBakedModel object = event.getModelRegistry().getObject(loc);
//
//        ModelResourceLocation loc2 = new ModelResourceLocation(this.testBlock.getRegistryName().toString(),
//                "not_normal");
//
//        IBakedModel object2 = event.getModelRegistry().getObject(loc2);
//
//        if (object == null || object2 == null){
//            System.out.println("ERROR: MODEL IS NULL");
//            System.exit(0);
//        }
//
//        MetaBlockModel customModel = new MetaBlockModel(object, object2);
//        event.getModelRegistry().putObject(loc, customModel);
//        event.getModelRegistry().putObject(loc2, customModel);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event){
        this.testBlock.registerItemModels();

//        ModelLoader.setCustomModelResourceLocation(testBlockItem, 0, new ModelResourceLocation("jmod:test", "inventory"));
//
//        ModelBakery.registerItemVariants(testBlockItem,
//                new ModelResourceLocation("jmod:test", "normal"),
//                new ModelResourceLocation("jmod:test", "not_normal")
//        );
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

    private void registerBlockColors(){
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();

        blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
            if (world != null && pos != null && state instanceof IExtendedBlockState && tintIndex == 0) {
                IExtendedBlockState extendedState = (IExtendedBlockState) state;

                Short id = extendedState.getValue(MetaBlock.ID);

                if (id != null){
                    switch (id){
                        case 1: return 0xFF0000;
                        case 2: return 0x00FF00;
                        case 3: return 0x0000FF;
                    }
                }
            }

            return 0xFFFFFF;
        }, this.testBlock);
    }

    private void registerItemColors(){
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

        itemColors.registerItemColorHandler(((stack, tintIndex) -> {
            if (tintIndex == 0){
                switch (stack.getMetadata()){
                    case 1: return 0xFF0000;
                    case 2: return 0x00FF00;
                    case 3: return 0x0000FF;
                }
            }

            return 0xFFFFFF;
        }), this.testBlock);
    }
}
