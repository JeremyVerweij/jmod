package com.jmod.core.client.model;

import com.jmod.core.client.utils.ModelUtils;
import com.jmod.core.common.block.PipeTestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.jspecify.annotations.NonNull;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.jmod.core.common.block.MetaBlock.BLOCK_SIZE;
import static com.jmod.core.common.block.MetaBlock.BLOCK_CENTER;

public class MetaPipeTestModel extends MetaBlockModel{
    private static final int ITEM_VARIANT = (1 << EnumFacing.EAST.getIndex()) | (1 << EnumFacing.WEST.getIndex());

    private final byte pipeStart;
    private final byte pipeEnd;

    private final Vector3f[] from;
    private final Vector3f[] to;

    private final List<BakedQuad>[] partialQuads;
    private final BakedQuad[] centerPartialQuads;
    private final BakedQuad[] sideOnlyPartialQuads;
    private final List<BakedQuad>[] overlayQuads;

    //NORTH: -Z, SOUTH: +Z, WEST: -X, EAST: +X
    @SuppressWarnings("unchecked")
    public MetaPipeTestModel(IBakedModel baseModel, int pipeSize) {
        super(baseModel, 64);

        byte pipeSizeOffset = (byte) (pipeSize / 2);
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
    protected List<BakedQuad> getQuadsFromExtendedState(@NonNull IExtendedBlockState state, @org.jspecify.annotations.Nullable EnumFacing side, long rand) {
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
        return ModelUtils.createQuad(positionSide, Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite("jmod:custom/pipe_overlay"), side.getIndex(), from, to);
    }

    private @Nonnull BakedQuad[] createCenterPartialQuads(){
        BakedQuad[] quads = new BakedQuad[6];

        for (int i = 0; i < 6; i++) {
            quads[i] = createQuad(new Vector3f(pipeStart, pipeStart, pipeStart),  new Vector3f(pipeEnd, pipeEnd, pipeEnd), EnumFacing.byIndex(i), 10);
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
                this.sideOnlyPartialQuads[side.getIndex()] = createQuad(from, to, EnumFacing.byIndex(i), 10);
            } else if (EnumFacing.byIndex(i) != side.getOpposite()) {
                quads.add(createQuad(from, to, EnumFacing.byIndex(i), 10));
            }
        }

        this.partialQuads[side.getIndex()] = quads;
    }

    private @Nonnull BakedQuad createQuad(@Nonnull Vector3f from, @Nonnull Vector3f to, @Nonnull EnumFacing side, int tintIndex){
        return ModelUtils.createQuad(side, Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite("minecraft:blocks/iron_block"), tintIndex, from, to);
    }
}
