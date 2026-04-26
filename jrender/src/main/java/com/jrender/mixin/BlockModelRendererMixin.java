package com.jrender.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.BitSet;
import java.util.List;

//TODO: Look into client.Minecraft to understand the game and render loop to find out where the vertex buffer is created to allow a new vertex format to be used in a custom shader
@Mixin(BlockModelRenderer.class)
public abstract class BlockModelRendererMixin {
    @Shadow
    @Final
    private BlockColors blockColors;

    @Shadow
    protected abstract void fillQuadBounds(IBlockState stateIn, int[] vertexData, EnumFacing face, float @Nullable [] quadBounds, BitSet boundsFlags);

    /**
     * @author jmod
     * @reason new render pipeline
     */
    @Overwrite
    public boolean renderModel(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand) {
        try {
            return this.jmod$renderModelNormal(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, posIn, stateIn);
            crashreportcategory.addCrashSection("Using AO", true);
            throw new ReportedException(crashreport);
        }
    }

    @Unique
    public boolean jmod$renderModelNormal(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand){
        boolean flag = false;
        BitSet bitset = new BitSet(3);

        for(EnumFacing enumfacing : EnumFacing.values()) {
            List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);
            if (!list.isEmpty() && (!checkSides || stateIn.shouldSideBeRendered(worldIn, posIn, enumfacing))) {
                int i = stateIn.getPackedLightmapCoords(worldIn, posIn.offset(enumfacing));
                this.jmod$renderQuadsNormal(worldIn, stateIn, posIn, i, false, buffer, list, bitset);
                flag = true;
            }
        }

        List<BakedQuad> list1 = modelIn.getQuads(stateIn, null, rand);
        if (!list1.isEmpty()) {
            this.jmod$renderQuadsNormal(worldIn, stateIn, posIn, -1, true, buffer, list1, bitset);
            flag = true;
        }

        return flag;
    }

    @Unique
    private void jmod$renderQuadsNormal(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, int brightnessIn, boolean ownBrightness, BufferBuilder buffer, List<BakedQuad> list, BitSet bitSet) {
        Vec3d vec3d = stateIn.getOffset(blockAccessIn, posIn);
        double d0 = (double)posIn.getX() + vec3d.x;
        double d1 = (double)posIn.getY() + vec3d.y;
        double d2 = (double)posIn.getZ() + vec3d.z;
        int i = 0;

        for(int j = list.size(); i < j; ++i) {
            BakedQuad bakedquad = list.get(i);
            if (ownBrightness) {
                this.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), null, bitSet);
                BlockPos blockpos = bitSet.get(0) ? posIn.offset(bakedquad.getFace()) : posIn;
                brightnessIn = stateIn.getPackedLightmapCoords(blockAccessIn, blockpos);
            }

            buffer.addVertexData(bakedquad.getVertexData());
            buffer.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);
            if (bakedquad.hasTintIndex()) {
                int k = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }

                int r = (k >> 16 & 255);
                int g = (k >> 8 & 255);
                int b = (k & 255);
                int a = 255;
                if (bakedquad.shouldApplyDiffuseLighting()) {
                    float diffuse = LightUtil.diffuseLight(bakedquad.getFace());
                    r = (int) (r * diffuse);
                    g = (int) (g * diffuse);
                    b = (int) (b * diffuse);
                }

                buffer.putColorRGBA(buffer.getColorIndex(4), r, g, b, a);
                buffer.putColorRGBA(buffer.getColorIndex(3), r, g, b, a);
                buffer.putColorRGBA(buffer.getColorIndex(2), r, g, b, a);
                buffer.putColorRGBA(buffer.getColorIndex(1), r, g, b, a);
            } else if (bakedquad.shouldApplyDiffuseLighting()) {
                float diffuse = LightUtil.diffuseLight(bakedquad.getFace());
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 4);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 3);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 2);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 1);
            }

            buffer.putPosition(d0, d1, d2);
        }

    }
}
