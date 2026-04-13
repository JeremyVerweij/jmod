package com.jmod.core.client.model;

import com.jmod.core.common.block.MetaBlock;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.List;

public class MetaBlockModel implements IBakedModel {
    private final IBakedModel standardModel;
    private final IBakedModel standardModel2;

    public MetaBlockModel(IBakedModel standardModel, IBakedModel standardModel2) {
        this.standardModel = standardModel;
        this.standardModel2 = standardModel2;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;

            Short id = extendedState.getValue(MetaBlock.ID);

            if (id != null && id > 0)
                return standardModel2.getQuads(state, side, rand);
        }

        return standardModel.getQuads(state, side, rand);
    }

    @Override public boolean isAmbientOcclusion() { return standardModel.isAmbientOcclusion(); }
    @Override public boolean isGui3d() { return standardModel.isGui3d(); }
    @Override public boolean isBuiltInRenderer() { return false; }
    @Override @MethodsReturnNonnullByDefault public TextureAtlasSprite getParticleTexture() { return standardModel.getParticleTexture(); }
    @Override @MethodsReturnNonnullByDefault public ItemCameraTransforms getItemCameraTransforms() { return standardModel.getItemCameraTransforms(); }
    @Override @MethodsReturnNonnullByDefault public ItemOverrideList getOverrides() { return standardModel.getOverrides(); }
}
