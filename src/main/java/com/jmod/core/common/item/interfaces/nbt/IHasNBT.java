package com.jmod.core.common.item.interfaces.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IHasNBT {
    default void ensureTag(ItemStack stack){
        if (!stack.hasTagCompound())
            createTag(stack);
    }

    default void createTag(ItemStack stack){
        stack.setTagCompound(new NBTTagCompound());
    }

    default NBTTagCompound getTag(ItemStack stack){
        this.ensureTag(stack);
        return stack.getTagCompound();
    }

    void addDefaultTags(NBTTagCompound tag);
}
