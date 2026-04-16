package com.jmod.core.client.model;

import com.jmod.core.client.utils.ModelUtils;
import com.jmod.core.common.block.PipeTestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
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

    private final List<BakedQuad>[] partialQuads;
    private final BakedQuad[] centerPartialQuads;
    private final BakedQuad[] sideOnlyPartialQuads;

    //NORTH: -Z, SOUTH: +Z, WEST: -X, EAST: +X
    @SuppressWarnings("unchecked")
    public MetaPipeTestModel(IBakedModel baseModel, int pipeSize) {
        super(baseModel, 64);

        byte pipeSizeOffset = (byte) (pipeSize / 2);
        this.pipeStart = (byte) (BLOCK_CENTER - pipeSizeOffset);
        this.pipeEnd = (byte) (BLOCK_CENTER + pipeSizeOffset);

        this.centerPartialQuads = createCenterPartialQuads();

        this.partialQuads = (List<BakedQuad>[]) new List[6];
        this.sideOnlyPartialQuads = new BakedQuad[6];

        generateQuadsForSide(new Vector3f(pipeStart, 0, pipeStart),  new Vector3f(pipeEnd, pipeStart, pipeEnd), EnumFacing.DOWN);
        generateQuadsForSide(new Vector3f(pipeStart, pipeEnd, pipeStart), new Vector3f(pipeEnd, BLOCK_SIZE, pipeEnd), EnumFacing.UP);
        generateQuadsForSide(new Vector3f(pipeStart, pipeStart, 0),  new Vector3f(pipeEnd, pipeEnd, pipeStart), EnumFacing.NORTH);
        generateQuadsForSide(new Vector3f(pipeStart, pipeStart, pipeEnd),  new Vector3f(pipeEnd, pipeEnd, BLOCK_SIZE), EnumFacing.SOUTH);
        generateQuadsForSide(new Vector3f(0, pipeStart, pipeStart),  new Vector3f(pipeStart, pipeEnd, pipeEnd), EnumFacing.WEST);
        generateQuadsForSide(new Vector3f(pipeEnd, pipeStart, pipeStart),  new Vector3f(BLOCK_SIZE, pipeEnd, pipeEnd), EnumFacing.EAST);

        this.init();
    }

    private @Nonnull BakedQuad[] createCenterPartialQuads(){
        BakedQuad[] quads = new BakedQuad[6];

        for (int i = 0; i < 6; i++) {
            quads[i] = createQuad(new Vector3f(pipeStart, pipeStart, pipeStart),  new Vector3f(pipeEnd, pipeEnd, pipeEnd), EnumFacing.byIndex(i));
        }

        return quads;
    }

    private void generateQuadsForSide(@Nonnull Vector3f from, @Nonnull Vector3f to, @Nonnull EnumFacing side){

        ArrayList<BakedQuad> quads = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (EnumFacing.byIndex(i) == side){
                this.sideOnlyPartialQuads[side.getIndex()] = createQuad(from, to, EnumFacing.byIndex(i));
            } else if (EnumFacing.byIndex(i) != side.getOpposite()) {
                quads.add(createQuad(from, to, EnumFacing.byIndex(i)));
            }
        }

        this.partialQuads[side.getIndex()] = quads;
    }

    private @Nonnull BakedQuad createQuad(@Nonnull Vector3f from, @Nonnull Vector3f to, @Nonnull EnumFacing side){
        return ModelUtils.createQuad(side, Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite("minecraft:blocks/iron_block"), 0, from, to);
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
            return id;

        return 0;
    }

    @Override
    public int getVariantFromItem(@Nonnull ItemStack stack) {
        return ITEM_VARIANT;
    }
}
