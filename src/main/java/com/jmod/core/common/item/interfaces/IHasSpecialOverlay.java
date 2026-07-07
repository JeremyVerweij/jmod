package com.jmod.core.common.item.interfaces;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.jmod.core.common.utils.random.RotationUtils.*;
import static com.jmod.core.common.utils.random.RotationUtils.isInBoundingBox2D;
import static com.jmod.core.common.utils.random.RotationUtils.rotateSideNoCorrection;

public interface IHasSpecialOverlay {
    boolean hasOverlay(EntityPlayer player, ItemStack heldItem);
    OverlayType getOverlayType(EntityPlayer player, ItemStack heldItem);

    default boolean overlayItemRightClick(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ){
        if (player.getHeldItem(hand).getItem() instanceof IHasSpecialOverlay specialOverlay){
            return switch (specialOverlay.getOverlayType(player, player.getHeldItem(hand))){
                case CONNECTIONS -> overlayItemRightClickConnectionType(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            };
        }

        return false;
    }

    default boolean overlayItemRightClickConnectionType(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ){
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IBlockHasConnectionOverlay connectionOverlayBlock){
            Vec2f UV = getUV(facing, hitX, hitY, hitZ);

            if (isInBoundingBox2D(UV, 0.2, 0.2, 0.8, 0.8)){
                connectionOverlayBlock.onOverlayClicked(state, worldIn, player, hand, pos, facing, 
                    getChainAmount(player, worldIn, pos, hand, facing, hitX, hitY, hitZ));
            } else if (isInBoundingBox2D(UV, 0.2, 0.8, 0.8, 1.0)){
                connectionOverlayBlock.onOverlayClicked(state, worldIn, player, hand, pos, rotateSideNoCorrection(facing, EnumSide2D.UP), 
                        getChainAmount(player, worldIn, pos, hand, facing, hitX, hitY, hitZ));
            } else if (isInBoundingBox2D(UV, 0.2, 0.0, 0.8, 0.2)){
                connectionOverlayBlock.onOverlayClicked(state, worldIn, player, hand, pos, rotateSideNoCorrection(facing, EnumSide2D.BOTTOM), 
                    getChainAmount(player, worldIn, pos, hand, facing, hitX, hitY, hitZ));
            } else if (isInBoundingBox2D(UV, 0.0, 0.2, 0.2, 0.8)){
                connectionOverlayBlock.onOverlayClicked(state, worldIn, player, hand, pos, rotateSideNoCorrection(facing, EnumSide2D.LEFT), 
                    getChainAmount(player, worldIn, pos, hand, facing, hitX, hitY, hitZ));
            } else if (isInBoundingBox2D(UV, 0.8, 0.2, 1.0, 0.8)){
                connectionOverlayBlock.onOverlayClicked(state, worldIn, player, hand, pos, rotateSideNoCorrection(facing, EnumSide2D.RIGHT), 
                    getChainAmount(player, worldIn, pos, hand, facing, hitX, hitY, hitZ));
            } else{
                connectionOverlayBlock.onOverlayClicked(state, worldIn, player, hand, pos, facing.getOpposite(),
                        getChainAmount(player, worldIn, pos, hand, facing, hitX, hitY, hitZ));
            }

            return true;
        }

        return false;
    }
    
    default int getChainAmount(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ){
        return 1;
    }

    enum OverlayType{
        CONNECTIONS
    }

    interface IBlockHasOverlay{
        OverlayType getType();
    }

    interface IBlockHasConnectionOverlay extends IBlockHasOverlay{
        void onOverlayClicked(IBlockState state, World world, EntityPlayer player, EnumHand hand, BlockPos pos, EnumFacing side, int chainsLeft);

        byte getSidesConnectedForOverlay(World world, BlockPos pos);

        @Override
        default OverlayType getType(){
            return OverlayType.CONNECTIONS;
        }
    }
}
