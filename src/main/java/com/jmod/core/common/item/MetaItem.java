package com.jmod.core.common.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class MetaItem extends Item {
    private final short maxId;
    private CreativeTabs metaBasedCreativeTab = null;

    public MetaItem(String modId, String registryName, short maxId) {
        this.setHasSubtypes(true);
        this.setRegistryName(modId, registryName);
        this.setTranslationKey(modId + "." + registryName + ".name");
        this.maxId = maxId;
    }

    @Override
    public @NonNull String getTranslationKey(@NonNull ItemStack stack) {
        return super.getTranslationKey(stack) + "." + stack.getMetadata();
    }

    protected @NonNull String getTranslationKeyWithoutMeta(@NonNull ItemStack stack){
        return super.getTranslationKey(stack);
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        for (int i = 0; i < this.getMaxId(); i++) {
            if(this.isItemInCreativeTab(i, tab)){
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public Item setCreativeTab(CreativeTabs tab) {
        this.metaBasedCreativeTab = tab;
        return this;
    }

    protected boolean isItemInCreativeTab(int subId, CreativeTabs tab){
        return this.metaBasedCreativeTab == tab || tab == CreativeTabs.SEARCH;
    }

    public void registerItemModels(){
        for (int i = 0; i < this.getMaxId(); i++) {
            this.registerItemModel(i);
        }
    }

    protected void registerItemModel(int id){
        ModelLoader.setCustomModelResourceLocation(this, id, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    public short getMaxId() {
        return maxId;
    }
}
