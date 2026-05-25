package com.jmod.jrender.client.render.world.block;

import com.jmod.jrender.client.util.ColorARGB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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

import java.util.BitSet;
import java.util.List;

public class JRenderBlockModelRenderer extends BlockModelRenderer {
    public JRenderBlockModelRenderer(BlockColors blockColorsIn) {
        super(blockColorsIn);
    }

    @Override
    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder buffer, boolean checkSides) {
        return super.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, buffer, checkSides);
    }

    @Override
    public boolean renderModel(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand) {
        boolean flag = Minecraft.isAmbientOcclusionEnabled() &&
                stateIn.getLightValue(worldIn, posIn) == 0 &&
                modelIn.isAmbientOcclusion(stateIn);

        flag = false; //disable AO for now.

        try {
            return flag ? this.renderModelSmooth(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand) :
                    this.renderModelFlat(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, posIn, stateIn);
            crashreportcategory.addCrashSection("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public boolean renderModelFlat(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand) {
        boolean flag = false;
        BitSet bitset = new BitSet(3);

        for(EnumFacing enumfacing : EnumFacing.values()) {
            List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);
            if (!list.isEmpty() && (!checkSides || stateIn.shouldSideBeRendered(worldIn, posIn, enumfacing))) {
                int i = stateIn.getPackedLightmapCoords(worldIn, posIn.offset(enumfacing));
                this.renderQuadsFlat(worldIn, stateIn, posIn, i, false, buffer, list, bitset);
                flag = true;
            }
        }

        List<BakedQuad> list1 = modelIn.getQuads(stateIn, null, rand);
        if (!list1.isEmpty()) {
            this.renderQuadsFlat(worldIn, stateIn, posIn, -1, true, buffer, list1, bitset);
            flag = true;
        }

        return flag;
    }

    protected void renderQuadsFlat(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, int brightnessIn,
                                   boolean ownBrightness, BufferBuilder buffer, List<BakedQuad> list, BitSet bitSet) {
        Vec3d vec3d = stateIn.getOffset(blockAccessIn, posIn);
        double quadX = (double)posIn.getX() + vec3d.x;
        double quadY = (double)posIn.getY() + vec3d.y;
        double quadZ = (double)posIn.getZ() + vec3d.z;
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
                int color = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    color = TextureUtil.anaglyphColor(color);
                }

                int colorAlpha = ColorARGB.unpackAlpha(color);
                int colorRed = ColorARGB.unpackRed(color);
                int colorGreen = ColorARGB.unpackGreen(color);
                int colorBlue = ColorARGB.unpackBlue(color);
                if (bakedquad.shouldApplyDiffuseLighting()) {
                    float diffuse = LightUtil.diffuseLight(bakedquad.getFace());
                    //diffuse is between 0 and 1, so it safe to multiple a byte by this float
                    colorRed = (int) ((float) colorRed * diffuse);
                    colorGreen = (int) ((float) colorGreen * diffuse);
                    colorBlue = (int) ((float) colorBlue * diffuse);
                }

                buffer.putColorRGBA(4, colorRed, colorGreen, colorBlue, colorAlpha);
                buffer.putColorRGBA(3, colorRed, colorGreen, colorBlue, colorAlpha);
                buffer.putColorRGBA(2, colorRed, colorGreen, colorBlue, colorAlpha);
                buffer.putColorRGBA(1, colorRed, colorGreen, colorBlue, colorAlpha);
            } else if (bakedquad.shouldApplyDiffuseLighting()) {
                float diffuse = LightUtil.diffuseLight(bakedquad.getFace());
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 4);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 3);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 2);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 1);
            }

            buffer.putPosition(quadX, quadY, quadZ);
        }

    }
}
