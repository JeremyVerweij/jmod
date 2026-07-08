package com.jmod.core.common.block.material;

import com.jmod.JMod;
import com.jmod.core.common.block.MetaBlock;
import com.jmod.core.common.block.interfaces.IMaterialBlockColor;
import com.jmod.core.common.item.ToolType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public abstract class MetaMaterialBlock extends MetaBlock implements IMaterialBlockColor {
    public MetaMaterialBlock(String modId, String registryName, Material blockMaterialIn, Set<ToolType> toolTypes) {
        super(modId, registryName, blockMaterialIn, (short) JMod.proxy.getMaterialRegistry().size(), toolTypes);
    }

    public MetaMaterialBlock(String modId, String registryName, Material blockMaterialIn, CreativeTabs creativeTab, Set<ToolType> toolTypes) {
        super(modId, registryName, blockMaterialIn, creativeTab, (short) JMod.proxy.getMaterialRegistry().size(), toolTypes);
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

        @Override
        public @NonNull String getTranslationKey(@NonNull ItemStack stack) {
            return super.getTranslationKeyWithoutMeta(stack);
        }

        @Override
        public @NonNull String getItemStackDisplayName(@NonNull ItemStack stack) {
            return I18n.format(this.getTranslationKey(stack), I18n.format(this.getMaterialTranslationKey(stack)));
        }

        protected String getMaterialTranslationKey(@NonNull ItemStack stack){
            if (stack.getMetadata() < JMod.proxy.getMaterialRegistry().size()){
                return JMod.proxy.getMaterialRegistry().toList().get(stack.getMetadata()).getMaterialTranslationKey();
            }

            return "jmod.materials.unknow";
        }
    }
}
