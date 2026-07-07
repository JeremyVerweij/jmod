package com.jmod.core.common.block.interfaces;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public interface IClientOnlyBlockEvents {
    void clientOnlyBlockPlace(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state,
                              @NotNull EntityLivingBase placer, @NotNull ItemStack stack);

    void clientOnlyBlockBreak(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, boolean isSameBlock);

    ItemStack getPickBlockClientOnly(@NotNull IBlockState state, @NotNull RayTraceResult target, @NotNull World world,
                           @NotNull BlockPos pos, @NotNull EntityPlayer player);
}
