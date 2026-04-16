package com.jmod.core.common.block;

import com.jmod.JMod;
import com.jmod.core.common.utils.unlisterProperty.UnlistedPropertyByte;
import com.jmod.core.proxy.ClientProxy;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class PipeTestBlock extends MetaBlock implements IWrenchable{
    public static final IUnlistedProperty<Byte> CONNECTIONS = new UnlistedPropertyByte("connections", (byte) 0, Byte.MAX_VALUE);
    private final static AxisAlignedBB PIPE_BOX = new AxisAlignedBB(4/16D, 4/16D, 4/16D, 12/16D, 12/16D, 12/16D);

    public PipeTestBlock() {
        super(JMod.MODID, "pipe", Material.IRON, CreativeTabs.BUILDING_BLOCKS, (short) 4);
    }

    @Override
    @MethodsReturnNonnullByDefault
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { ID, CONNECTIONS });
    }

    @Override
    @MethodsReturnNonnullByDefault
    public IBlockState getExtendedState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;
            return extendedState.withProperty(ID, (short) (((ClientProxy) JMod.proxy).clientMetaIdHolder
                    .getId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().world.provider.getDimension()) & 0b111_1111_1111_1111))
                    .withProperty(CONNECTIONS, (byte) ((((ClientProxy) JMod.proxy).clientMetaIdHolder
                            .getId(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().world.provider.getDimension()) >> 16) & 0b111111));
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
    public EnumActionResult onWrenchUse(IBlockState state, World world, EntityPlayer player, EnumHand hand, int x, int y, int z, EnumFacing side) {
        if (!world.isRemote){
            int meta = this.getServerMetaData(x, y, z, world.provider.getDimension()) ^ (1 << (side.getIndex() + 16));

            this.setServerMetaData(x, y, z, world.provider.getDimension(), meta);
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public byte getSidesConnectForOverlay(World world, BlockPos pos) {
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
            if (neighbour.getBlock() instanceof PipeTestBlock){
                int neighbourMeta = getServerMetaData(neighbourPos, world.provider.getDimension());
                setServerMetaData(neighbourPos, world.provider.getDimension(),
                        neighbourMeta | (1 << (EnumFacing.byIndex(i).getOpposite().getIndex() + 16)));
                meta |= 1 << (i + 16);
            }
        }

        setServerMetaData(pos, world.provider.getDimension(), meta);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return PIPE_BOX;
    }
}
