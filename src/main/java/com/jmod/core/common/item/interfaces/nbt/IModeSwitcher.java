package com.jmod.core.common.item.interfaces.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IModeSwitcher<T extends Enum<T> & IModeSwitcher.ModeEnum> extends IHasNBT{
    String MODE_NBT_TAG = "mode";

    @Override
    default void addDefaultTags(NBTTagCompound tag){
        tag.setInteger(MODE_NBT_TAG, 0);
    }

    default T getMode(ItemStack stack){
        this.ensureTag(stack);
        int mode = this.getTag(stack).getInteger(MODE_NBT_TAG);

        return getModes()[mode];
    }

    default T setMode(ItemStack stack, T mode){
        this.ensureTag(stack);
        getTag(stack).setInteger(MODE_NBT_TAG, mode.ordinal());
        return mode;
    }

    default T toggleModes(ItemStack stack){
        T mode = getMode(stack);
        T[] values = getModes();
        int index = mode.ordinal() + 1;

        if (index >= values.length) index = 0;

        return setMode(stack, values[index]);
    }

    T[] getModes();

    interface ModeEnum{
        String getTranslationKey();
    }
}
