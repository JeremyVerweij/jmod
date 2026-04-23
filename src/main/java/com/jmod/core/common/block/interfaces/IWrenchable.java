package com.jmod.core.common.block.interfaces;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWrenchable {
    EnumActionResult onWrenchUse(IBlockState state, World world, EntityPlayer player, EnumHand hand, int x, int y, int z, EnumFacing side);

    byte getSidesConnectForOverlay(World world, BlockPos pos);
}
