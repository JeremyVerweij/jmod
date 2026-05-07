package com.jmod.core.common.item;

import com.jmod.core.common.block.interfaces.IWrenchable;
import com.jmod.jmod.Reference;
import com.jmod.core.common.item.material.MetaMaterialToolItem;
import com.jmod.core.common.material.Material;
import com.jmod.core.common.material.MaterialProperties;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.jmod.core.common.utils.random.RotationUtils.*;

public class WrenchItem extends MetaMaterialToolItem {
    public WrenchItem() {
        super(Reference.MOD_ID, "wrench", ToolType.PICKAXE);
    }

    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IWrenchable){
            Vec2f UV = getUV(facing, hitX, hitY, hitZ);

            if (!worldIn.isRemote && !player.isCreative()){
                damageItemStack(player.getHeldItem(hand), 1);
            }

            if (isInBoundingBox2D(UV, 0.2, 0.2, 0.8, 0.8)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos, facing);
            } else if (isInBoundingBox2D(UV, 0.2, 0.8, 0.8, 1.0)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos, rotateSideNoCorrection(facing, EnumSide2D.UP));
            } else if (isInBoundingBox2D(UV, 0.2, 0.0, 0.8, 0.2)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos, rotateSideNoCorrection(facing, EnumSide2D.BOTTOM));
            } else if (isInBoundingBox2D(UV, 0.0, 0.2, 0.2, 0.8)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos, rotateSideNoCorrection(facing, EnumSide2D.LEFT));
            } else if (isInBoundingBox2D(UV, 0.8, 0.2, 1.0, 0.8)){
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos, rotateSideNoCorrection(facing, EnumSide2D.RIGHT));
            } else{
                return ((IWrenchable) block).onWrenchUse(state, worldIn, player, hand, pos, facing.getOpposite());
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    protected boolean isEnabled(Material material) {
        return material.getProperty(MaterialProperties.HAS_TOOLS);
    }
}
