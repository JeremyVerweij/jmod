package com.jmod.core.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public interface IServerOnlyBlockEvents {
    void serverOnlyBlockPlace(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                              @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack);

    void serverOnlyBlockBreak(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isSameBlock);

    ItemStack getPickBlockServerOnly(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world,
                           @Nonnull BlockPos pos, @Nonnull EntityPlayer player);

    void serverOnlyOnPlayerHarvested(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player);
}
