package com.jmod.core.common.item;

import com.jmod.Tags;
import com.jmod.core.common.item.interfaces.IHasSpecialOverlay;
import com.jmod.core.common.item.interfaces.nbt.IModeSwitcher;
import com.jmod.core.common.item.material.MetaMaterialToolItem;
import com.jmod.core.common.material.Material;
import com.jmod.core.common.material.MaterialProperties;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import org.jspecify.annotations.Nullable;

import java.util.List;

public class WrenchItem extends MetaMaterialToolItem implements IHasSpecialOverlay, IModeSwitcher<WrenchItem.WrenchMode> {
    public WrenchItem() {
        super(Tags.MOD_ID, "wrench", ToolType.PICKAXE);
    }

    @Override
    public void addInformation(@NonNull ItemStack stack, @Nullable World worldIn, List<String> tooltip, @NonNull ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.jmod.wrench.modeTooltip", I18n.format(getMode(stack).getTranslationKey())));

        super.addInformation(stack, worldIn, tooltip, flagIn);
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
                WrenchMode mode = this.toggleModes(stack);
                player.sendMessage(new TextComponentTranslation("item.jmod.wrench.modeSwitch", I18n.format(mode.getTranslationKey())));

            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public int getChainAmount(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ){
        ItemStack stack = player.getHeldItem(hand);
        WrenchMode mode = this.getMode(stack);

        return mode == WrenchMode.MULTI ? 8 : 1;
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

    @Override
    public void addDefaultTags(NBTTagCompound tag) {
        IModeSwitcher.super.addDefaultTags(tag);
        super.addDefaultTags(tag);
    }

    @Override
    public WrenchMode[] getModes() {
        return WrenchMode.values();
    }

    public enum WrenchMode implements ModeEnum{
        SINGLE("jmod.mode.single"),
        MULTI("jmod.mode.mutli");

        private final String translationKey;

        WrenchMode(String translationKey) {
            this.translationKey = translationKey;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }
    }
}
