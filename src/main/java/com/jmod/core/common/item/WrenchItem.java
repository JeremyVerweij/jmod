package com.jmod.core.common.item;

import com.jmod.Tags;
import com.jmod.core.common.item.interfaces.IHasSpecialOverlay;
import com.jmod.core.common.item.material.MetaMaterialToolItem;
import com.jmod.core.common.material.Material;
import com.jmod.core.common.material.MaterialProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class WrenchItem extends MetaMaterialToolItem implements IHasSpecialOverlay {
    public WrenchItem() {
        super(Tags.MOD_ID, "wrench", ToolType.PICKAXE);
    }

    @Override
    public EnumActionResult onItemUse(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (IHasSpecialOverlay.overlayItemRightClick(player, worldIn, pos, hand, facing, hitX, hitY, hitZ) &&
                !worldIn.isRemote && !player.isCreative()){
            damageItemStack(player.getHeldItem(hand), 1);
        }

        return EnumActionResult.PASS;
    }

    @Override
    protected boolean isEnabled(Material material) {
        return material.getProperty(MaterialProperties.HAS_TOOLS);
    }

    @Override
    public boolean hasOverlay(EntityPlayer player, ItemStack heldItem) {
        return true;
    }

    @Override
    public OverlayType getOverlayType(EntityPlayer player, ItemStack heldItem) {
        return OverlayType.CONNECTIONS;
    }
}
