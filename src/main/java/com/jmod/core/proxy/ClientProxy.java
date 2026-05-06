package com.jmod.core.proxy;

import com.jmod.JMod;
import com.jmod.core.client.ClientMetaIdHolder;
import com.jmod.core.common.block.MetaBlock;
import com.jmod.core.common.block.interfaces.IHasColor;
import com.jmod.jmod.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
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
        ModelResourceLocation cube_all = new ModelResourceLocation("minecraft:stone", "normal");
        IBakedModel normalObject = event.getModelRegistry().getObject(cube_all);

        for (MetaBlock metaBlock : this.metaBlockRegisterEvent.getRegistry()) {
            registerModel(event, metaBlock, metaBlock.getModel(normalObject));
        }
    }

    public void registerModel(ModelBakeEvent event, Block block, IBakedModel model){
        ModelResourceLocation normal = new ModelResourceLocation(block.getRegistryName().toString(), "normal");
        ModelResourceLocation inventory = new ModelResourceLocation(block.getRegistryName().toString(), "inventory");

        event.getModelRegistry().putObject(normal, model);
        event.getModelRegistry().putObject(inventory, model);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event){
        for (MetaBlock metaBlock : this.metaBlockRegisterEvent.getRegistry()) {
            metaBlock.registerItemModels();
        }
    }

    @SubscribeEvent
    public void registerTextures(TextureStitchEvent.Pre event){
        registerTexture(event, new ResourceLocation(Reference.MOD_ID, "block/pipe"));
    }

    public void registerTexture(TextureStitchEvent.Pre event, ResourceLocation loc){
        event.getMap().registerSprite(loc);
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
        for (MetaBlock metaBlock : this.metaBlockRegisterEvent.getRegistry()) {
            if (metaBlock instanceof IHasColor color){
                blockColors.registerBlockColorHandler(color::getColorBlock, metaBlock);
            }
        }
    }

    private void registerItemColors(){
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

        for (MetaBlock metaBlock : this.metaBlockRegisterEvent.getRegistry()) {
            if (metaBlock instanceof IHasColor color){
                itemColors.registerItemColorHandler(color::getColorItem, metaBlock);
            }
        }
    }
}
