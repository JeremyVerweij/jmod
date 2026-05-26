package com.jmod.mixin.jrender.core;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockColors.class)
public abstract class BlockColorsMixin {
    @Shadow
    public abstract void registerBlockColorHandler(IBlockColor blockColor, Block... blocksIn);

    @Shadow
    public abstract int colorMultiplier(IBlockState state, @Nullable IBlockAccess blockAccess, @Nullable BlockPos pos, int tintIndex);

    /**
     * @author jrender
     * @reason Minecraft is stupid and does not utilize the alpha channel
     */
    @Overwrite
    public static BlockColors init() {
        final BlockColors blockcolors = new BlockColors();
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = state.getValue(BlockDoublePlant.VARIANT);
            return (worldIn != null && pos != null &&
                    (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS ||
                            blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.FERN) ?
                    BiomeColorHelper.getGrassColorAtPos(worldIn, state.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER
                                                                 ? pos.down() : pos) : -1) | 0xFF000000;
        }, Blocks.DOUBLE_PLANT);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            if (worldIn != null && pos != null) {
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityFlowerPot) {
                    Item item = ((TileEntityFlowerPot) tileentity).getFlowerPotItem();
                    IBlockState iblockstate = Block.getBlockFromItem(item).getDefaultState();
                    return blockcolors.colorMultiplier(iblockstate, worldIn, pos, tintIndex);
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }, Blocks.FLOWER_POT);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> (worldIn != null && pos != null ?
                BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5F, 1.0F)) | 0xFF000000, Blocks.GRASS);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            BlockPlanks.EnumType blockplanks$enumtype = state.getValue(BlockOldLeaf.VARIANT);
            if (blockplanks$enumtype == BlockPlanks.EnumType.SPRUCE) {
                return ColorizerFoliage.getFoliageColorPine() | 0xFF000000;
            } else if (blockplanks$enumtype == BlockPlanks.EnumType.BIRCH) {
                return ColorizerFoliage.getFoliageColorBirch() | 0xFF000000;
            } else {
                return (worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic()) | 0xFF000000;
            }
        }, Blocks.LEAVES);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> (worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic()) | 0xFF000000, Blocks.LEAVES2);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> (worldIn != null && pos != null ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : -1) | 0xFF000000, Blocks.WATER, Blocks.FLOWING_WATER);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> (BlockRedstoneWire.colorMultiplier((Integer) state.getValue(BlockRedstoneWire.POWER))) | 0xFF000000, Blocks.REDSTONE_WIRE);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> (worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : -1) | 0xFF000000, Blocks.REEDS);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            int i = state.getValue(BlockStem.AGE);
            int j = i * 32;
            int k = 255 - i * 8;
            int l = i * 4;
            return (j << 16 | k << 8 | l) | 0xFF000000;
        }, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            if (worldIn != null && pos != null) {
                return BiomeColorHelper.getGrassColorAtPos(worldIn, pos) | 0xFF000000;
            } else {
                return (state.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.DEAD_BUSH ? 16777215 : ColorizerGrass.getGrassColor(0.5F, 1.0F)) | 0xFF000000;
            }
        }, Blocks.TALLGRASS);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> (worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic()) | 0xFF000000, Blocks.VINE);
        blockcolors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> (worldIn != null && pos != null ? 2129968 : 7455580) | 0xFF000000, Blocks.WATERLILY);
        ForgeHooksClient.onBlockColorsInit(blockcolors);
        return blockcolors;
    }
}
