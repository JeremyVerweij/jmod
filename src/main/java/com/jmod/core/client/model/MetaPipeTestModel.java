package com.jmod.core.client.model;

import com.jmod.core.client.utils.ModelUtils;
import com.jmod.core.common.block.PipeTestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.jspecify.annotations.NonNull;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.jmod.core.client.utils.ModelUtils.resizeUVForLargerTexture;
import static com.jmod.core.common.block.MetaBlock.BLOCK_SIZE;
import static com.jmod.core.common.block.MetaBlock.BLOCK_CENTER;

public class MetaPipeTestModel extends MetaBlockModel{
    private static final int ITEM_VARIANT = (1 << EnumFacing.EAST.getIndex()) | (1 << EnumFacing.WEST.getIndex());
    private static final float ATLAS_SIZE = 32f;
    private static final float[] ARROW_UV = new float[]{0, 0, 12, 12};
    private static final float[] CENTER_UV = new float[]{20, 0, 32, 12};
    private static final float[] ARM_END_12_UV = new float[]{20, 12, 32, 24};
    private static final float[] ARM_END_10_UV = new float[]{10, 22, 20, 32};
    private static final float[] ARM_END_8_UV = new float[]{20, 24, 28, 32};
    private static final float[] ARM_END_6_UV = new float[]{14, 16, 20, 22};
    private static final float[] ARM_END_4_UV = new float[]{16, 12, 20, 16};
    private static final float[] ARM_12_UV = new float[]{0, 18, 12, 20};
    private static final float[] ARM_10_UV = new float[]{0, 20, 10, 23};
    private static final float[] ARM_8_UV = new float[]{0, 23, 8, 27};
    private static final float[] ARM_6_UV = new float[]{0, 27, 6, 32};
    private static final float[] ARM_4_UV = new float[]{0, 12, 4, 18};

    private final byte pipeStart;
    private final byte pipeEnd;
    private final byte pipeSize;

    private final Vector3f[] from;
    private final Vector3f[] to;

    private final List<BakedQuad>[] partialQuads;
    private final BakedQuad[] centerPartialQuads;
    private final BakedQuad[] sideOnlyPartialQuads;
    private final List<BakedQuad>[] overlayQuads;

    private static float[] getArmUVFromPipeSize(int pipeSize){
        return switch (pipeSize){
            case 12 -> ARM_12_UV;
            case 10 -> ARM_10_UV;
            case 8 -> ARM_8_UV;
            case 6 -> ARM_6_UV;
            default -> ARM_4_UV;
        };
    }

    private static float[] getEndArmUVFromPipeSize(int pipeSize){
        return switch (pipeSize){
            case 12 -> ARM_END_12_UV;
            case 10 -> ARM_END_10_UV;
            case 8 -> ARM_END_8_UV;
            case 6 -> ARM_END_6_UV;
            default -> ARM_END_4_UV;
        };
    }

    private static float[] getCenterUVFromPipeSize(int pipeSize){
        int halfSize = pipeSize >> 1;
        int offset = 7 - halfSize;

        return new float[]{CENTER_UV[0] + offset, CENTER_UV[1] + offset, CENTER_UV[2] - offset, CENTER_UV[3] - offset};
    }

    //NORTH: -Z, SOUTH: +Z, WEST: -X, EAST: +X
    @SuppressWarnings("unchecked")
    public MetaPipeTestModel(IBakedModel baseModel, int pipeSize) {
        super(baseModel, 64);

        byte pipeSizeOffset = (byte) (pipeSize / 2);
        this.pipeSize = (byte) pipeSize;
        this.pipeStart = (byte) (BLOCK_CENTER - pipeSizeOffset);
        this.pipeEnd = (byte) (BLOCK_CENTER + pipeSizeOffset);

        this.from = new Vector3f[EnumFacing.VALUES.length];
        this.to = new Vector3f[EnumFacing.VALUES.length];
        
        this.generateFromAndTo();
        
        this.centerPartialQuads = createCenterPartialQuads();

        this.partialQuads = (List<BakedQuad>[]) new List[6];
        this.sideOnlyPartialQuads = new BakedQuad[6];
        this.overlayQuads = createOverlayQuads();

        generateQuadsForSide(EnumFacing.DOWN);
        generateQuadsForSide(EnumFacing.UP);
        generateQuadsForSide(EnumFacing.NORTH);
        generateQuadsForSide(EnumFacing.SOUTH);
        generateQuadsForSide(EnumFacing.WEST);
        generateQuadsForSide(EnumFacing.EAST);

        this.init();
    }

    @Override
    protected List<BakedQuad> getQuadsFromExtendedState(@NonNull IExtendedBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> bakedQuads = new ArrayList<>(super.getQuadsFromExtendedState(state, side, rand));

        int restrictions = getRestrictionsFromState(state);

        for (int i = 0; i < EnumFacing.values().length; i++) {
            int index = 1 << i;
            if ((restrictions & index) > 0){
                bakedQuads.addAll(this.overlayQuads[i]);
            }
        }

        return bakedQuads;
    }

    @Override
    public List<BakedQuad> getQuadsForVariant(int variant, @Nullable EnumFacing side) {
        List<BakedQuad> list = new ArrayList<>();

        if (side == null){
            for (int i = 0; i < 6; i++) {
                if ((variant & (1 << i)) == 0)
                    list.add(this.centerPartialQuads[i]);
            }

            for (int i = 0; i < 6; i++) {
                if ((variant & (1 << i)) > 0){
                    list.addAll(this.partialQuads[i]);
                }
            }
        }else if ((variant & (1 << side.getIndex())) > 0){
            list.add(this.sideOnlyPartialQuads[side.getIndex()]);
        }

        return list;
    }

    @Override
    public int getVariantFromState(@Nonnull IExtendedBlockState state) {
        Byte id = state.getValue(PipeTestBlock.CONNECTIONS);

        if (id != null)
            return id &0b111111;

        return 0;
    }

    @Override
    public int getVariantFromItem(@Nonnull ItemStack stack) {
        return ITEM_VARIANT;
    }

    private int getRestrictionsFromState(@Nonnull IExtendedBlockState state) {
        Byte id = state.getValue(PipeTestBlock.RESTRICTIONS);

        if (id != null)
            return id &0b111111;

        return 0;
    }

    private void generateFromAndTo(){
        int index = 0;

        this.from[index] = new Vector3f(pipeStart, 0, pipeStart);
        this.to[index++] = new Vector3f(pipeEnd, pipeStart, pipeEnd);

        this.from[index] = new Vector3f(pipeStart, pipeEnd, pipeStart);
        this.to[index++] = new Vector3f(pipeEnd, BLOCK_SIZE, pipeEnd);

        this.from[index] = new Vector3f(pipeStart, pipeStart, 0);
        this.to[index++] = new Vector3f(pipeEnd, pipeEnd, pipeStart);

        this.from[index] = new Vector3f(pipeStart, pipeStart, pipeEnd);
        this.to[index++] = new Vector3f(pipeEnd, pipeEnd, BLOCK_SIZE);

        this.from[index] = new Vector3f(0, pipeStart, pipeStart);
        this.to[index++] = new Vector3f(pipeStart, pipeEnd, pipeEnd);

        this.from[index] = new Vector3f(pipeEnd, pipeStart, pipeStart);
        this.to[index++] = new Vector3f(BLOCK_SIZE, pipeEnd, pipeEnd);
    }

    private List<BakedQuad>[] createOverlayQuads() {
        //noinspection unchecked
        List<BakedQuad>[] bakedQuads = (List<BakedQuad>[]) new ArrayList[EnumFacing.values().length];

        for (int i = 0; i < EnumFacing.values().length; i++) {
            List<BakedQuad> quads = new ArrayList<>();
            EnumFacing side = EnumFacing.byIndex(i);
            Vector3f from = this.from[i];
            Vector3f to = this.to[i];

            for (int j = 0; j < EnumFacing.values().length; j++) {
                if (j == side.getIndex() || j == side.getOpposite().getIndex())
                    continue;

                quads.add(createOverlayQuad(side, EnumFacing.byIndex(j), from, to));
            }

            bakedQuads[i] = quads;
        }

        return bakedQuads;
    }

    private BakedQuad createOverlayQuad(@Nonnull EnumFacing side, @Nonnull EnumFacing positionSide, Vector3f from, Vector3f to){
        float offset = 0.01f;
        Vector3f fromOverlay = new Vector3f(from.x - offset, from.y - offset, from.z - offset);
        Vector3f toOverlay = new Vector3f(to.x + offset, to.y + offset, to.z + offset);

        resizeOverlay(fromOverlay, toOverlay, side, positionSide);

        BakedQuad quad = ModelUtils.createQuad(positionSide, Minecraft.getMinecraft().getTextureMapBlocks()
                        .getAtlasSprite("jmod:block/pipe"), side.getIndex(), fromOverlay, toOverlay,
                resizeUVForLargerTexture(ATLAS_SIZE, ARROW_UV));

        return rotateQuadUV(quad, side, positionSide);
    }

    private void resizeOverlay(Vector3f from, Vector3f to, EnumFacing side, EnumFacing positionSide){
        float sizeOffset = 1f;
        float fromX = from.x, fromY = from.y, fromZ = from.z;
        float toX = to.x, toY = to.y, toZ = to.z;

        if (positionSide == EnumFacing.NORTH || positionSide == EnumFacing.SOUTH){
            if (side == EnumFacing.UP || side == EnumFacing.DOWN){
                from.x = (float) Math.floor((fromX + toX) / 2f) - sizeOffset;
                to.x = (float) Math.floor((fromX + toX) / 2f) + sizeOffset;
            }else{
                from.y = (float) Math.floor((fromY + toY) / 2f) - sizeOffset;
                to.y = (float) Math.floor((fromY + toY) / 2f) + sizeOffset;
            }
        } else if (positionSide == EnumFacing.EAST || positionSide == EnumFacing.WEST){
            if (side == EnumFacing.UP || side == EnumFacing.DOWN){
                from.z = (float) Math.floor((fromZ + toZ) / 2f) - sizeOffset;
                to.z = (float) Math.floor((fromZ + toZ) / 2f) + sizeOffset;
            }else{
                from.y = (float) Math.floor((fromY + toY) / 2f) - sizeOffset;
                to.y = (float) Math.floor((fromY + toY) / 2f) + sizeOffset;
            }
        } else{
            if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH){
                from.x = (float) Math.floor((fromX + toX) / 2f) - sizeOffset;
                to.x = (float) Math.floor((fromX + toX) / 2f) + sizeOffset;
            }else{
                from.z = (float) Math.floor((fromZ + toZ) / 2f) - sizeOffset;
                to.z = (float) Math.floor((fromZ + toZ) / 2f) + sizeOffset;
            }
        }

        switch (side) {
            case DOWN -> {
                to.y = sizeOffset * 2;
            }
            case UP -> {
                from.y = toY - (sizeOffset * 2);
            }
            case NORTH -> {
                to.z = sizeOffset * 2;
            }
            case SOUTH -> {
                from.z = toZ - (sizeOffset * 2);
            }
            case WEST -> {
                to.x = sizeOffset * 2;
            }
            case EAST -> {
                from.x = toX - (sizeOffset * 2);
            }
        }
    }

    private BakedQuad rotateQuadUV(BakedQuad quad, EnumFacing side, EnumFacing positionSide){
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(quad.getFormat());
        UVTransformer transformer = new UVTransformer(builder, getRotation(side, positionSide));

        quad.pipe(transformer);
        transformer.apply();

        return builder.build();
    }

    private UVTransformer.UVMapper getRotation(EnumFacing side, EnumFacing positionSide) {
        if (side == EnumFacing.DOWN ||
                (positionSide == EnumFacing.DOWN && side == EnumFacing.NORTH) ||
                (positionSide == EnumFacing.UP && side == EnumFacing.SOUTH)) {
            return UVTransformer.UVMapper.create180DegreeRotation();
        } else if ((side == EnumFacing.EAST && positionSide != EnumFacing.NORTH) ||
                (side == EnumFacing.WEST && positionSide == EnumFacing.NORTH) ||
                (side == EnumFacing.NORTH && positionSide == EnumFacing.EAST) ||
                (side == EnumFacing.SOUTH && positionSide == EnumFacing.WEST)) {
            return UVTransformer.UVMapper.create90DegreeRotation();
        } else if ((side == EnumFacing.WEST || side == EnumFacing.EAST||
                (side == EnumFacing.NORTH && positionSide == EnumFacing.WEST) ||
                (side == EnumFacing.SOUTH && positionSide == EnumFacing.EAST))) {
            return UVTransformer.UVMapper.create270DegreeRotation();
        }


        return UVTransformer.UVMapper.create0DegreeRotation();
    }

    private @Nonnull BakedQuad[] createCenterPartialQuads(){
        BakedQuad[] quads = new BakedQuad[6];

        for (int i = 0; i < 6; i++) {
            quads[i] = createCenterPipeQuad(new Vector3f(pipeStart, pipeStart, pipeStart),  new Vector3f(pipeEnd, pipeEnd, pipeEnd), EnumFacing.byIndex(i));
        }

        return quads;
    }

    private void generateQuadsForSide(@Nonnull EnumFacing side){
        generateQuadsForSide(this.from[side.getIndex()], this.to[side.getIndex()], side);
    }

    private void generateQuadsForSide(@Nonnull Vector3f from, @Nonnull Vector3f to, @Nonnull EnumFacing side){
        ArrayList<BakedQuad> quads = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (EnumFacing.byIndex(i) == side){
                this.sideOnlyPartialQuads[side.getIndex()] = createSidedPipeQuad(from, to, EnumFacing.byIndex(i));
            } else if (EnumFacing.byIndex(i) != side.getOpposite()) {
                quads.add(createArmPipeQuad(from, to, side, EnumFacing.byIndex(i)));
            }
        }

        this.partialQuads[side.getIndex()] = quads;
    }

    private @Nonnull BakedQuad createCenterPipeQuad(@Nonnull Vector3f from, @Nonnull Vector3f to, @Nonnull EnumFacing side){
        return ModelUtils.createQuad(side, Minecraft.getMinecraft().getTextureMapBlocks()
                        .getAtlasSprite("jmod:block/pipe"), 10, from, to,
                resizeUVForLargerTexture(ATLAS_SIZE, getCenterUVFromPipeSize(this.pipeSize)));
    }

    private @Nonnull BakedQuad createSidedPipeQuad(@Nonnull Vector3f from, @Nonnull Vector3f to, @Nonnull EnumFacing side){
        return ModelUtils.createQuad(side, Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite("jmod:block/pipe"), 10, from, to,
                resizeUVForLargerTexture(ATLAS_SIZE, getEndArmUVFromPipeSize(this.pipeSize)));
    }

    private @Nonnull BakedQuad createArmPipeQuad(@Nonnull Vector3f from, @Nonnull Vector3f to, @Nonnull EnumFacing side, EnumFacing positionSide){
        BakedQuad quad = ModelUtils.createQuad(positionSide, Minecraft.getMinecraft().getTextureMapBlocks()
                        .getAtlasSprite("jmod:block/pipe"), 10, from, to,
                resizeUVForLargerTexture(ATLAS_SIZE, getArmUVFromPipeSize(this.pipeSize)));

        return rotateQuadUV(quad, side, positionSide);
    }
}
