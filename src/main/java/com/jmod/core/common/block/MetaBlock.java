package com.jmod.core.common.block;

import com.jmod.JMod;
import com.jmod.core.common.net.MetaIdsDeltaAddPacket;
import com.jmod.core.common.net.MetaIdsDeltaDeletePacket;
import com.jmod.core.common.net.NetworkHandler;
import com.jmod.core.proxy.ClientProxy;
import com.jmod.core.common.utils.UnlistedPropertyShort;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;

public class MetaBlock extends Block implements IServerOnlyBlockEvents, IClientOnlyBlockEvents{
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

    public Item getItemBlock(){
        return this.itemBlock;
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (!world.isRemote){
            this.serverOnlyBlockPlace(world, pos, state, placer, stack);
        }else{
            this.clientOnlyBlockPlace(world, pos, state, placer, stack);
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (world.getBlockState(pos).getBlock() != state.getBlock()){
            if (!world.isRemote){
                this.serverOnlyBlockBreak(world, pos, state);
            }else{
                this.clientOnlyBlockBreak(world, pos, state);
            }
        }


        super.breakBlock(world, pos, state);
    }

    @Override
    @MethodsReturnNonnullByDefault
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { ID });
    }

    @Override
    @MethodsReturnNonnullByDefault
    public IBlockState getExtendedState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        if (state instanceof IExtendedBlockState && Minecraft.getMinecraft().world.isRemote) {
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
    public void serverOnlyBlockBreak(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        NetworkHandler.sendToClientsTracking(new MetaIdsDeltaDeletePacket(pos.getX(), pos.getY(), pos.getZ()),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 0));

        JMod.proxy.getServerMetaIdHolder().invalidateBlock(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension());
    }

    public short getMaxId() {
        return maxId;
    }

    @Override
    public void clientOnlyBlockPlace(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
        ((ClientProxy) JMod.proxy).clientMetaIdHolder.
                putId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().player.dimension, stack.getMetadata());
    }

    @Override
    public void clientOnlyBlockBreak(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

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
        @MethodsReturnNonnullByDefault
        public String getTranslationKey(@Nonnull ItemStack stack) {
            return super.getTranslationKey(stack) + stack.getMetadata();
        }

        @Override
        public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
            super.getSubItems(tab, items);

            if (this.isInCreativeTab(tab)){
                for (int i = 0; i < ((MetaBlock) this.block).maxId; i++) {
                    items.add(new ItemStack(this, 1, i));
                }
            }
        }
    }
}
