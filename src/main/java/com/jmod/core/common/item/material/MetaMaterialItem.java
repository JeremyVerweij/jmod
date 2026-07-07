package com.jmod.core.common.item.material;

import com.jmod.JMod;
import com.jmod.core.common.item.MetaItem;
import com.jmod.core.common.item.interfaces.IHasItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public abstract class MetaMaterialItem extends MetaItem implements IHasItemColor {
    public MetaMaterialItem(String modId, String registryName) {
        super(modId, registryName, (short) JMod.proxy.getMaterialRegistry().size());
    }

    @Override
    public int getColorItem(ItemStack stack, int tintIndex) {
        if (tintIndex == 10 && stack.getMetadata() < JMod.proxy.getMaterialRegistry().size())
            return JMod.proxy.getMaterialRegistry().toList().get(stack.getMetadata()).getColor();
        else return 0xFFFFFFFF;
    }

    protected abstract boolean isEnabled(com.jmod.core.common.material.Material material);

    @Override
    protected boolean isItemInCreativeTab(int subId, CreativeTabs tab) {
        if (!this.isEnabled(JMod.proxy.getMaterialRegistry().toList().get(subId)))
            return false;

        return super.isItemInCreativeTab(subId, tab);
    }
}
