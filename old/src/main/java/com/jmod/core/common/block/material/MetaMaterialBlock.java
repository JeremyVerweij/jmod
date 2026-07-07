package com.jmod.core.common.block.material;

import com.jmod.JMod;
import com.jmod.core.common.block.MetaBlock;
import com.jmod.core.common.block.interfaces.IMaterialBlockColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class MetaMaterialBlock extends MetaBlock implements IMaterialBlockColor {
    public MetaMaterialBlock(String modId, String registryName, Material blockMaterialIn) {
        super(modId, registryName, blockMaterialIn, (short) JMod.proxy.getMaterialRegistry().size());
    }

    public MetaMaterialBlock(String modId, String registryName, Material blockMaterialIn, CreativeTabs creativeTab) {
        super(modId, registryName, blockMaterialIn, creativeTab, (short) JMod.proxy.getMaterialRegistry().size());
    }

    protected abstract boolean isEnabled(com.jmod.core.common.material.Material material);

    @Override
    protected Item createItemBlock() {
        return new ItemMetaMaterialBlock(this).setRegistryName(this.getRegistryName());
    }

    protected static class ItemMetaMaterialBlock extends ItemMetaBlock{
        public ItemMetaMaterialBlock(MetaBlock block) {
            super(block);
        }

        @Override
        protected boolean isItemInCreativeTab(int subId, CreativeTabs tab) {
            if (!((MetaMaterialBlock) this.block).isEnabled(JMod.proxy.getMaterialRegistry().toList().get(subId)))
                return false;

            return super.isItemInCreativeTab(subId, tab);
        }
    }
}
