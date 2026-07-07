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
import org.jetbrains.annotations.NotNull;

public class SplitSideBlock extends Block implements IClientOnlyBlockEvents, IServerOnlyBlockEvents {
    public SplitSideBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    public SplitSideBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public void onBlockPlacedBy(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state,
                                @NotNull EntityLivingBase placer, @NotNull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (!world.isRemote){
            this.serverOnlyBlockPlace(world, pos, state, placer, stack);
        }else{
            this.clientOnlyBlockPlace(world, pos, state, placer, stack);
        }
    }

    @Override
    public void breakBlock(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!world.isRemote){
            this.serverOnlyBlockBreak(world, pos, state, world.getBlockState(pos).getBlock() == state.getBlock());
        }else{
            this.clientOnlyBlockBreak(world, pos, state, world.getBlockState(pos).getBlock() == state.getBlock());
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public ItemStack getPickBlock(@NotNull IBlockState state, @NotNull RayTraceResult target, @NotNull World world,
                                  @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        if (world.isRemote){
            return this.getPickBlockClientOnly(state, target, world, pos, player);
        }else{
            return this.getPickBlockServerOnly(state, target, world, pos, player);
        }
    }

    @Override
    public void onBlockHarvested(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player) {
        if (worldIn.isRemote){

        }else{
            this.serverOnlyOnPlayerHarvested(worldIn, pos, state, player);
        }
    }

    @Override
    public void clientOnlyBlockPlace(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityLivingBase placer, @NotNull ItemStack stack) {

    }

    @Override
    public void clientOnlyBlockBreak(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, boolean isSameBlock) {

    }

    @Override
    public ItemStack getPickBlockClientOnly(@NotNull IBlockState state, @NotNull RayTraceResult target, @NotNull World world, @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void serverOnlyBlockPlace(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityLivingBase placer, @NotNull ItemStack stack) {

    }

    @Override
    public void serverOnlyBlockBreak(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, boolean isSameBlock) {

    }

    @Override
    public ItemStack getPickBlockServerOnly(@NotNull IBlockState state, @NotNull RayTraceResult target, @NotNull World world, @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void serverOnlyOnPlayerHarvested(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player) {

    }
}
