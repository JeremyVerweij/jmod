package com.jmod.core.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.List;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {
    @Shadow
    protected abstract void fillQuadBounds(IBlockState stateIn, int[] vertexData, EnumFacing face, @Nullable float[] quadBounds, BitSet boundsFlags);

    @Shadow
    @Final
    private BlockColors blockColors;

    @Overwrite
    private void renderQuadsSmooth(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, List<BakedQuad> list, float[] quadBounds, BitSet bitSet, BlockModelRenderer.AmbientOcclusionFace aoFace) {
        Vec3d vec3d = stateIn.getOffset(blockAccessIn, posIn);
        double d0 = (double) posIn.getX() + vec3d.x;
        double d1 = (double) posIn.getY() + vec3d.y;
        double d2 = (double) posIn.getZ() + vec3d.z;
        int i = 0;

        for (int j = list.size(); i < j; ++i) {
            BakedQuad bakedquad = list.get(i);
            this.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), quadBounds, bitSet);
            aoFace.updateVertexBrightness(blockAccessIn, stateIn, posIn, bakedquad.getFace(), quadBounds, bitSet);
            buffer.addVertexData(bakedquad.getVertexData());
            buffer.putBrightness4(aoFace.vertexBrightness[0], aoFace.vertexBrightness[1], aoFace.vertexBrightness[2], aoFace.vertexBrightness[3]);
            if (bakedquad.shouldApplyDiffuseLighting()) {
                float diffuse = LightUtil.diffuseLight(bakedquad.getFace());
                aoFace.vertexColorMultiplier[0] *= diffuse;
                aoFace.vertexColorMultiplier[1] *= diffuse;
                aoFace.vertexColorMultiplier[2] *= diffuse;
                aoFace.vertexColorMultiplier[3] *= diffuse;
            }
            if (bakedquad.hasTintIndex()) {
                int k = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }

                float f = (float) (k >> 16 & 255) / 255.0F;
                float f1 = (float) (k >> 8 & 255) / 255.0F;
                float f2 = (float) (k & 255) / 255.0F;
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[0] * f, aoFace.vertexColorMultiplier[0] * f1, aoFace.vertexColorMultiplier[0] * f2, 4);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[1] * f, aoFace.vertexColorMultiplier[1] * f1, aoFace.vertexColorMultiplier[1] * f2, 3);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[2] * f, aoFace.vertexColorMultiplier[2] * f1, aoFace.vertexColorMultiplier[2] * f2, 2);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[3] * f, aoFace.vertexColorMultiplier[3] * f1, aoFace.vertexColorMultiplier[3] * f2, 1);
            } else {
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[0], 4);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[1], 3);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[2], 2);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[3], aoFace.vertexColorMultiplier[3], aoFace.vertexColorMultiplier[3], 1);
            }

            buffer.putPosition(d0, d1, d2);
        }
    }
}
