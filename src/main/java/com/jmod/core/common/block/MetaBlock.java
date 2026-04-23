package com.jmod.core.common.block;

import com.jmod.JMod;
import com.jmod.core.common.net.MetaIdsDeltaAddPacket;
import com.jmod.core.common.net.MetaIdsDeltaDeletePacket;
import com.jmod.core.common.net.NetworkHandler;
import com.jmod.core.proxy.ClientProxy;
import com.jmod.core.common.utils.unlisterProperty.UnlistedPropertyShort;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;

public class MetaBlock extends SplitSideBlock {
    public final static byte BLOCK_SIZE = 16;
    public final static byte BLOCK_CENTER = BLOCK_SIZE / 2;
    public static final IUnlistedProperty<Short> ID = new UnlistedPropertyShort("id", (short) 0, Short.MAX_VALUE);
    private final short maxId;
    private final Item itemBlock;

    public MetaBlock(String modId, String registryName, Material blockMaterialIn, short maxId) {
        super(blockMaterialIn);
        this.setRegistryName(modId, registryName);
        this.setTranslationKey(modId + "." + registryName);
        this.maxId = maxId;

        this.itemBlock = new ItemMetaBlock(this)
                .setRegistryName(this.getRegistryName());
    }

    public MetaBlock(String modId, String registryName, Material blockMaterialIn, CreativeTabs creativeTab, short maxId){
        this(modId, registryName, blockMaterialIn, maxId);
        this.setCreativeTab(creativeTab);
    }

    public short getMaxId() {
        return maxId;
    }

    public Item getItemBlock(){
        return this.itemBlock;
    }

    public int getServerMetaData(int x, int y, int z, int dimension){
        return JMod.proxy.getServerMetaIdHolder().getId(x, y, z, dimension);
    }

    public int getServerMetaData(BlockPos pos, int dimension){
        return this.getServerMetaData(pos.getX(), pos.getY(), pos.getZ(), dimension);
    }

    public void setServerMetaData(int x, int y, int z, int dimension, int meta){
        NetworkHandler.sendToClientsTracking(new MetaIdsDeltaAddPacket(x, y, z, meta),
                new NetworkRegistry.TargetPoint(dimension, x, y, z, 0));

        JMod.proxy.getServerMetaIdHolder().putId(x, y, z, dimension, meta);
    }

    public void setServerMetaData(BlockPos pos, int dimension, int meta){
        this.setServerMetaData(pos.getX(), pos.getY(), pos.getZ(), dimension, meta);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { ID });
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;
            return extendedState.withProperty(ID, (short) (((ClientProxy) JMod.proxy).clientMetaIdHolder
                    .getId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().world.provider.getDimension()) & 0b111_1111_1111_1111));
        }
        return state;
    }

    @Override
    public void serverOnlyBlockPlace(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
        short id = (short) (stack.getMetadata() & Short.MAX_VALUE);

        //fallback for when someone messes with metadata
        if (id > getMaxId()) id = 0;

        NetworkHandler.sendToClientsTracking(new MetaIdsDeltaAddPacket(pos.getX(), pos.getY(), pos.getZ(), id),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 0));

        JMod.proxy.getServerMetaIdHolder().putId(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension(), id);
    }

    @Override
    public void serverOnlyOnPlayerHarvested(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player) {
        if (player.isCreative()) return;

        short id = (short) (JMod.proxy.getServerMetaIdHolder().getId(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension()) & Short.MAX_VALUE);

        ItemStack drop = new ItemStack(this.itemBlock, 1, id);

        spawnAsEntity(world, pos, drop);
    }

    @Override
    public void serverOnlyBlockBreak(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isSameBlock) {
        NetworkHandler.sendToClientsTracking(new MetaIdsDeltaDeletePacket(pos.getX(), pos.getY(), pos.getZ()),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 0));

        JMod.proxy.getServerMetaIdHolder().invalidateBlock(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension());
    }

    @Override
    public ItemStack getPickBlockServerOnly(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        short id = (short) (JMod.proxy.getServerMetaIdHolder().getId(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension()) & Short.MAX_VALUE);
        return new ItemStack(this.itemBlock, 1, id);
    }

    @Override
    public void clientOnlyBlockPlace(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
        ((ClientProxy) JMod.proxy).clientMetaIdHolder.
                putId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().player.dimension, stack.getMetadata());
    }

    @Override
    public ItemStack getPickBlockClientOnly(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        short id = (short) (((ClientProxy) JMod.proxy).clientMetaIdHolder.
                getId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().player.dimension) & Short.MAX_VALUE);
        return new ItemStack(this.itemBlock, 1, id);
    }

    public void registerItemModels(){
        for (int i = 0; i < this.maxId; i++) {
            this.registerItemModel(i);
        }
    }

    protected void registerItemModel(int id){
        ModelLoader.setCustomModelResourceLocation(this.itemBlock, id, new ModelResourceLocation(this.itemBlock.getRegistryName(), "inventory"));
    }

    public static class ItemMetaBlock extends ItemBlock{
        public ItemMetaBlock(MetaBlock block) {
            super(block);
            this.setHasSubtypes(true);
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

        @Override
        public int getMetadata(ItemStack stack) {
            return stack.getItemDamage();
        }

        @Override
        public String getTranslationKey(@Nonnull ItemStack stack) {
            return super.getTranslationKey(stack) + stack.getMetadata();
        }

        @Override
        public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
            if (this.isInCreativeTab(tab)){
                for (int i = 0; i < ((MetaBlock) this.block).maxId; i++) {
                    items.add(new ItemStack(this, 1, i));
                }
            }
        }
    }
}
