package com.jmod.core.common.block.interfaces;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public interface IClientOnlyBlockEvents {
    void clientOnlyBlockPlace(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                              @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack);

    void clientOnlyBlockBreak(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isSameBlock);

    ItemStack getPickBlockClientOnly(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world,
                           @Nonnull BlockPos pos, @Nonnull EntityPlayer player);
}
