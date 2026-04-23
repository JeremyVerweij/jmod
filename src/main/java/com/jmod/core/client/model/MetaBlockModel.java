package com.jmod.core.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class MetaBlockModel implements IBakedModel {
    private final IBakedModel baseModel;
    private final List<BakedQuad>[][] quads;
    private final ItemMetaBlockModel[] itemModels;
    private final int maxVariants;

    @SuppressWarnings("unchecked")
    public MetaBlockModel(IBakedModel baseModel, int maxVariants) {
        this.baseModel = baseModel;
        this.quads = (List<BakedQuad>[][]) new List[7][maxVariants];
        this.itemModels = new ItemMetaBlockModel[maxVariants];
        this.maxVariants = maxVariants;
    }

    public abstract List<BakedQuad> getQuadsForVariant(int variant, @Nullable EnumFacing side);

    public abstract int getVariantFromState(@Nonnull IExtendedBlockState state);

    public abstract int getVariantFromItem(@Nonnull ItemStack stack);

    public void init(){
        for (int i = 0; i < this.maxVariants; i++) {
            for (int j = 0; j < 7; j++) {
                if (j == 0)
                    this.quads[j][i] = getQuadsForVariant(i, null);
                else
                    this.quads[j][i] = getQuadsForVariant(i, EnumFacing.byIndex(j - 1));
            }

            this.itemModels[i] = new ItemMetaBlockModel(i);
        }
    }

    protected List<BakedQuad> getQuadsFromExtendedState(@Nonnull IExtendedBlockState state, @Nullable EnumFacing side, long rand){
        int variant = getVariantFromState(state);

        if (side == null){
            return this.quads[0][variant];
        }

        return this.quads[side.getIndex() + 1][variant];
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;
            return getQuadsFromExtendedState(extendedState, side, rand);
        }

        return baseModel.getQuads(state, side, rand);
    }

    @Override public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Collections.emptyList()){
            @Override
            public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                int itemVariant = MetaBlockModel.this.getVariantFromItem(stack);
                if (itemVariant < 0 || itemVariant >= itemModels.length) {
                    return MetaBlockModel.this.itemModels[0]; // Fallback to avoid out-of-bounds crash
                }
                return MetaBlockModel.this.itemModels[itemVariant];
            }
        };
    }

    protected List<BakedQuad> getItemQuads(@Nullable EnumFacing side, long rand, int variant){
        if (side == null){
            return MetaBlockModel.this.quads[0][variant];
        }

        return MetaBlockModel.this.quads[side.getIndex() + 1][variant];
    }

    @Override public boolean isAmbientOcclusion() { return baseModel.isAmbientOcclusion(); }
    @Override public boolean isGui3d() { return baseModel.isGui3d(); }
    @Override public boolean isBuiltInRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleTexture() { return baseModel.getParticleTexture(); }
    @Override public ItemCameraTransforms getItemCameraTransforms() {
        return baseModel.getItemCameraTransforms();
    }

    class ItemMetaBlockModel implements IBakedModel{
        private final int variant;

        public ItemMetaBlockModel(int variant){
            this.variant = variant;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            return MetaBlockModel.this.getItemQuads(side, rand, this.variant);
        }

        @Override
        public boolean isAmbientOcclusion() {
            return MetaBlockModel.this.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return MetaBlockModel.this.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return MetaBlockModel.this.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return MetaBlockModel.this.getParticleTexture();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return MetaBlockModel.this.getOverrides();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return MetaBlockModel.this.getItemCameraTransforms();
        }
    }
}
