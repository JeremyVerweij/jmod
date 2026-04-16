package com.jmod.core.proxy;

import com.jmod.core.client.ClientMetaIdHolder;
import com.jmod.core.client.model.MetaBlockModel;
import com.jmod.core.client.model.MetaPipeTestModel;
import com.jmod.core.common.block.MetaBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
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
        ModelResourceLocation cube_all = new ModelResourceLocation("minecraft:stone",
                "normal");

        ModelResourceLocation normal = new ModelResourceLocation(this.pipeBlock.getRegistryName().toString(),
                "normal");

        ModelResourceLocation inventory = new ModelResourceLocation(this.pipeBlock.getRegistryName().toString(),
                "inventory");

        IBakedModel normalObject = event.getModelRegistry().getObject(cube_all);
        MetaBlockModel customModel = new MetaPipeTestModel(normalObject, 6);
        event.getModelRegistry().putObject(normal, customModel);
        event.getModelRegistry().putObject(inventory, customModel);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event){
        this.testBlock.registerItemModels();
        this.pipeBlock.registerItemModels();
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
        }, this.testBlock, this.pipeBlock);
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
        }), this.testBlock, this.pipeBlock);
    }
}
