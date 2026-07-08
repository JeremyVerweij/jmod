package com.jmod.core.common.item.interfaces.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IDamageable extends IHasNBT {
    String NBT_DAMAGE_TAG = "damage";

    @Override
    default void addDefaultTags(NBTTagCompound tag){
        tag.setInteger(NBT_DAMAGE_TAG, 0);
    }

    default int getDamageFromItemStack(ItemStack stack){
        this.ensureTag(stack);
        return this.getTag(stack).getInteger(NBT_DAMAGE_TAG);
    }

    default void setDamageToItemStack(ItemStack stack, int damage){
        this.ensureTag(stack);
        this.getTag(stack).setInteger(NBT_DAMAGE_TAG, damage);
    }

    default void damageItemStack(ItemStack stack, int damage){
        setDamageToItemStack(stack, getDamageFromItemStack(stack) + damage);

        if (getDamageFromItemStack(stack) > getMaxDamageFromItemStack(stack)){
            stack.shrink(1);
        }
    }

    int getMaxDamageFromItemStack(ItemStack stack);
}
