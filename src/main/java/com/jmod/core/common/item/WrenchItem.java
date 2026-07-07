package com.jmod.core.common.item;

import com.jmod.Tags;
import com.jmod.core.common.item.interfaces.IHasSpecialOverlay;
import com.jmod.core.common.item.material.MetaMaterialToolItem;
import com.jmod.core.common.material.Material;
import com.jmod.core.common.material.MaterialProperties;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class WrenchItem extends MetaMaterialToolItem implements IHasSpecialOverlay {
    private static final String MODE_NBT_TAG = "mode";

    public WrenchItem() {
        super(Tags.MOD_ID, "wrench", ToolType.PICKAXE);
    }

    @Override
    public @NonNull EnumActionResult onItemUse(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.overlayItemRightClick(player, worldIn, pos, hand, facing, hitX, hitY, hitZ) &&
                !worldIn.isRemote && !player.isCreative()){
            damageItemStack(player.getHeldItem(hand), 1);
        }

        return EnumActionResult.PASS;
    }

    @Override
    public @NonNull ActionResult<ItemStack> onItemRightClick(@NonNull World world, @NonNull EntityPlayer player, @NonNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote) {
            RayTraceResult raytrace = this.rayTrace(world, player, true);

            if (raytrace == null || raytrace.typeOfHit == RayTraceResult.Type.MISS) {
                if (!stack.hasTagCompound())
                    createTag(stack);

                boolean mode = stack.getTagCompound().getBoolean(MODE_NBT_TAG);
                stack.getTagCompound().setBoolean(MODE_NBT_TAG, !mode);

                player.sendMessage(new TextComponentTranslation("jcore.item.wrench.modeSwitch", I18n.format(mode ? "jcore.item.mode.single" : "jcore.item.mode.mutli")));

            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public int getChainAmount(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ){
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.hasTagCompound())
            createTag(stack);

        boolean mode = stack.getTagCompound().getBoolean(MODE_NBT_TAG);

        return mode ? 8 : 1;
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
