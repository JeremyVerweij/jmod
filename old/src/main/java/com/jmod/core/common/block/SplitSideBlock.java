package com.jmod.core.common.block;

import com.jmod.core.common.block.interfaces.IClientOnlyBlockEvents;
import com.jmod.core.common.block.interfaces.IServerOnlyBlockEvents;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SplitSideBlock extends Block implements IClientOnlyBlockEvents, IServerOnlyBlockEvents {
    public SplitSideBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public SplitSideBlock(Material materialIn) {
        super(materialIn);
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
        if (!world.isRemote){
            this.serverOnlyBlockBreak(world, pos, state, world.getBlockState(pos).getBlock() == state.getBlock());
        }else{
            this.clientOnlyBlockBreak(world, pos, state, world.getBlockState(pos).getBlock() == state.getBlock());
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world,
                                  @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        if (world.isRemote){
            return this.getPickBlockClientOnly(state, target, world, pos, player);
        }else{
            return this.getPickBlockServerOnly(state, target, world, pos, player);
        }
    }

    @Override
    public void onBlockHarvested(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player) {
        if (worldIn.isRemote){

        }else{
            this.serverOnlyOnPlayerHarvested(worldIn, pos, state, player);
        }
    }

    @Override
    public void clientOnlyBlockPlace(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {

    }

    @Override
    public void clientOnlyBlockBreak(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isSameBlock) {

    }

    @Override
    public ItemStack getPickBlockClientOnly(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void serverOnlyBlockPlace(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {

    }

    @Override
    public void serverOnlyBlockBreak(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isSameBlock) {

    }

    @Override
    public ItemStack getPickBlockServerOnly(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void serverOnlyOnPlayerHarvested(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player) {

    }
}
