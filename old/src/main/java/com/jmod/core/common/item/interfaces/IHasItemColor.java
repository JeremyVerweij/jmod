package com.jmod.core.common.item.interfaces;

import net.minecraft.item.ItemStack;

public interface IHasItemColor {
    int getColorItem(ItemStack stack, int tintIndex);
}
