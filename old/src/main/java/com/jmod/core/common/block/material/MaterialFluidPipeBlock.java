package com.jmod.core.common.block.material;

import com.jmod.JMod;
import com.jmod.core.client.model.MetaPipeTestModel;
import com.jmod.core.common.block.interfaces.IRequireTool;
import com.jmod.core.common.item.ToolType;
import com.jmod.core.common.item.interfaces.IHasSpecialOverlay;
import com.jmod.core.common.material.MaterialProperties;
import com.jmod.core.common.utils.MiningTier;
import com.jmod.core.common.utils.unlisterProperty.UnlistedPropertyByte;
import com.jmod.core.proxy.ClientProxy;
import com.jmod.jmod.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class MaterialFluidPipeBlock extends MetaMaterialBlock implements IHasSpecialOverlay.IBlockHasConnectionOverlay, IRequireTool {
    public static final IUnlistedProperty<Byte> CONNECTIONS = new UnlistedPropertyByte("connections", (byte) 0, Byte.MAX_VALUE);
    public static final IUnlistedProperty<Byte> RESTRICTIONS = new UnlistedPropertyByte("restrictions", (byte) 0, Byte.MAX_VALUE);

    private static AxisAlignedBB createBoundingBox(Vector3f from, Vector3f to){
        return new AxisAlignedBB(from.x / 16D, from.y / 16D, from.z / 16D,
                to.x / 16D, to.y / 16D, to.z / 16D);
    }

    private final int size;
    private final AxisAlignedBB centerBoundingBox;
    private final AxisAlignedBB[] connectionBoundingBoxes;

    public MaterialFluidPipeBlock(int size) {
        super(Reference.MOD_ID, "pipe_" + size, Material.ROCK, CreativeTabs.BUILDING_BLOCKS);

        setHardness(5.0f);

        this.size = size;

        int offset = 8 - (size / 2);

        this.centerBoundingBox = createBoundingBox(new Vector3f(offset, offset, offset),
                new Vector3f(16 - offset, 16 - offset, 16 - offset));

        this.connectionBoundingBoxes = new AxisAlignedBB[EnumFacing.VALUES.length];

        this.connectionBoundingBoxes[EnumFacing.UP.getIndex()] = createBoundingBox(new Vector3f(offset, offset, offset),
                new Vector3f(16 - offset, 16, 16 - offset));

        this.connectionBoundingBoxes[EnumFacing.SOUTH.getIndex()] = createBoundingBox(new Vector3f(offset, offset, offset),
                new Vector3f(16 - offset, 16 - offset, 16));

        this.connectionBoundingBoxes[EnumFacing.EAST.getIndex()] = createBoundingBox(new Vector3f(offset, offset, offset),
                new Vector3f(16, 16 - offset, 16 - offset));

        this.connectionBoundingBoxes[EnumFacing.DOWN.getIndex()] = createBoundingBox(new Vector3f(offset, 0, offset),
                new Vector3f(16 - offset, 16 - offset, 16 - offset));

        this.connectionBoundingBoxes[EnumFacing.NORTH.getIndex()] = createBoundingBox(new Vector3f(offset, offset, 0),
                new Vector3f(16 - offset, 16 - offset, 16 - offset));

        this.connectionBoundingBoxes[EnumFacing.WEST.getIndex()] = createBoundingBox(new Vector3f(0, offset, offset),
                new Vector3f(16 - offset, 16 - offset, 16 - offset));
    }

    @Override
    protected boolean isEnabled(com.jmod.core.common.material.Material material) {
        return material.getProperty(MaterialProperties.HAS_FLUID_PIPE);
    }

    @Override
    public IBakedModel getModel(IBakedModel normalModel) {
        return new MetaPipeTestModel(normalModel, size);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { ID, CONNECTIONS, RESTRICTIONS });
    }

    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;
            return extendedState.withProperty(ID, (short) (((ClientProxy) JMod.proxy).clientMetaIdHolder
                    .getId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().world.provider.getDimension()) & 0b111_1111_1111_1111))
                    .withProperty(CONNECTIONS, (byte) ((((ClientProxy) JMod.proxy).clientMetaIdHolder
                            .getId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().world.provider.getDimension()) >> 16) & 0b111111))
                    .withProperty(RESTRICTIONS, (byte) ((((ClientProxy) JMod.proxy).clientMetaIdHolder
                            .getId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().world.provider.getDimension()) >> 22) & 0b111111));
        }
        return state;
    }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public void onOverlayClicked(IBlockState state, World world, EntityPlayer player, EnumHand hand, BlockPos pos, EnumFacing side) {
        if (!world.isRemote){
            int meta = this.getServerMetaData(pos, world.provider.getDimension());

            if (player.isSneaking()){
                meta ^= (1 << (side.getIndex() + 22)); // add/remove restriction
                meta |= (1 << (side.getIndex() + 16)); // add connection
            }else{
                meta ^= (1 << (side.getIndex() + 16)); // add/remove connection
                meta &= ~(1 << (side.getIndex() + 22)); // remove restriction
            }

            BlockPos neighbourPos = pos.offset(side);
            IBlockState neighbour = world.getBlockState(neighbourPos);
            if (neighbour.getBlock() instanceof MaterialFluidPipeBlock && (meta & (1 << (side.getIndex() + 16))) > 0){
                int neighbourMeta = this.getServerMetaData(neighbourPos, world.provider.getDimension());
                neighbourMeta |= (1 << (side.getOpposite().getIndex() + 16));
                this.setServerMetaData(neighbourPos, world.provider.getDimension(), neighbourMeta);
            }

            this.setServerMetaData(pos, world.provider.getDimension(), meta);
        }
    }

    @Override
    public byte getSidesConnectedForOverlay(World world, BlockPos pos) {
        int id = (((ClientProxy) JMod.proxy).clientMetaIdHolder.getId(pos, world.provider.getDimension()) >> 16) & 0b111111;

        byte valid = 0;

        for (int i = 0; i < 6; i++) {
            byte mask = (byte) (1 << i);
            byte orMask = (byte) (id & mask);
            valid |= orMask;
        }

        return valid;
    }

    @Override
    public void serverOnlyBlockPlace(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
        super.serverOnlyBlockPlace(world, pos, state, placer, stack);

        int meta = getServerMetaData(pos, world.provider.getDimension());

        for (int i = 0; i < 6; i++) {
            BlockPos neighbourPos = pos.offset(EnumFacing.byIndex(i));
            IBlockState neighbour = world.getBlockState(neighbourPos);
            if (neighbour.getBlock() instanceof MaterialFluidPipeBlock){
                int neighbourMeta = getServerMetaData(neighbourPos, world.provider.getDimension());
                setServerMetaData(neighbourPos, world.provider.getDimension(),
                        neighbourMeta | (1 << (EnumFacing.byIndex(i).getOpposite().getIndex() + 16)));
                meta |= 1 << (i + 16);
            }
        }

        setServerMetaData(pos, world.provider.getDimension(), meta);
    }

    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        int id;

        if (worldIn.isRemote){
            id = ((ClientProxy) JMod.proxy).clientMetaIdHolder.getId(pos, worldIn.provider.getDimension());
        }else{
            id = JMod.proxy.getServerMetaIdHolder().getId(pos.getX(), pos.getY(), pos.getZ(), worldIn.provider.getDimension());
        }

        byte connections = (byte) ((id >> 16) & 0b111111);

        for (EnumFacing side : EnumFacing.values()) {
            if (((1 << side.getIndex()) & connections) > 0){
                addCollisionBoxToList(pos, entityBox, collidingBoxes, this.connectionBoundingBoxes[side.getIndex()]);
            }
        }

        addCollisionBoxToList(pos, entityBox, collidingBoxes, this.centerBoundingBox);
    }

    @Override
    public int getColorBlockFallback(IExtendedBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        if (tintIndex < EnumFacing.VALUES.length){
            int restrictions = getRestrictionsFromState(state);

            return (restrictions & (1 << tintIndex)) > 0 ? 0xFFFFFFFF : 0;
        }

        return super.getColorBlockFallback(state, world, pos, tintIndex);
    }

    @Override
    public int getColorItemFallback(ItemStack stack, int tintIndex) {
        if (tintIndex < EnumFacing.VALUES.length){
            return 0;
        }

        return super.getColorItemFallback(stack, tintIndex);
    }

    private int getRestrictionsFromState(@Nonnull IExtendedBlockState state) {
        Byte id = state.getValue(MaterialFluidPipeBlock.RESTRICTIONS);

        if (id != null)
            return id & 0b111111;

        return 0;
    }

//    @Override
//    public void addToDebug(List<String> lines, IExtendedBlockState extendedState) {
//        super.addToDebug(lines, extendedState);
//
//        Byte connections = extendedState.getValue(CONNECTIONS);
//        Byte restrictions = extendedState.getValue(RESTRICTIONS);
//
//        if (connections != null && restrictions != null){
//            for (int i = 0; i < EnumFacing.values().length; i++) {
//                boolean isConnected = (connections & (1 << i)) > 0;
//                boolean isRestricted = (restrictions & (1 << i)) > 0;
//
//                lines.add(TextFormatting.RESET + EnumFacing.byIndex(i).toString() + ": {con: " +
//                        (isConnected ? TextFormatting.GREEN + " " : TextFormatting.RED) + isConnected + TextFormatting.RESET +
//                        ", res: " + (isRestricted ? TextFormatting.GREEN + " " : TextFormatting.RED) + isRestricted + "}" + TextFormatting.RESET);
//            }
//        }
//    }

    @Override
    public MiningTier toolLevel() {
        return MiningTier.IRON;
    }

    @Override
    public ToolType toolType() {
        return ToolType.PICKAXE;
    }
}
