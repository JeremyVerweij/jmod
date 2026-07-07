package com.jmod.core.common.block.interfaces;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IHasBlockColor {
    int getColorBlock(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex);
    int getColorItem(ItemStack stack, int tintIndex);
}
