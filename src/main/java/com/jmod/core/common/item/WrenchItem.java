package com.jmod.core.common.item;

import com.jmod.JMod;
import com.jmod.core.common.block.interfaces.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.jmod.core.common.utils.random.RotationUtils.*;

public class WrenchItem extends Item {
    public WrenchItem() {
        this.setRegistryName(JMod.MODID, "wrench");
        this.setTranslationKey(JMod.MODID + ".wrench");
    }

    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof IWrenchable){
            Vec2f UV = getUV(facing, hitX, hitY, hitZ);

            if (isInBoundingBox2D(UV, 0.2, 0.2, 0.8, 0.8)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos.getX(), pos.getY(), pos.getZ(), facing);
            } else if (isInBoundingBox2D(UV, 0.2, 0.8, 0.8, 1.0)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos.getX(), pos.getY(), pos.getZ(), rotateSideNoCorrection(facing, EnumSide2D.UP));
            } else if (isInBoundingBox2D(UV, 0.2, 0.0, 0.8, 0.2)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos.getX(), pos.getY(), pos.getZ(), rotateSideNoCorrection(facing, EnumSide2D.BOTTOM));
            } else if (isInBoundingBox2D(UV, 0.0, 0.2, 0.2, 0.8)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos.getX(), pos.getY(), pos.getZ(), rotateSideNoCorrection(facing, EnumSide2D.LEFT));
            } else if (isInBoundingBox2D(UV, 0.8, 0.2, 1.0, 0.8)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos.getX(), pos.getY(), pos.getZ(), rotateSideNoCorrection(facing, EnumSide2D.RIGHT));
            } else{
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos.getX(), pos.getY(), pos.getZ(), facing.getOpposite());
            }
        }

        return EnumActionResult.FAIL;
    }
}
