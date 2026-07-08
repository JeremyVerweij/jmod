package com.jmod.core.common.item.interfaces;

import com.jmod.core.common.item.ToolType;
import com.jmod.core.common.utils.MiningTier;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IToolItem {
    MiningTier getToolTier(@NotNull ItemStack stack);
    ToolType getToolType();
}
