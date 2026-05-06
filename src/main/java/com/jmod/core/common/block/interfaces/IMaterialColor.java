package com.jmod.core.common.block.interfaces;

import com.jmod.JMod;
import com.jmod.core.common.block.MetaBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;

public interface IMaterialColor extends IHasColor{
    default int getColorBlock(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex){
        IExtendedBlockState extendedState = (IExtendedBlockState) state;

        Short id = extendedState.getValue(MetaBlock.ID);

        if (id != null && tintIndex == 10) return getColor(id);

        return 0xFFFFFFFF;
    }

    default int getColorItem(ItemStack stack, int tintIndex){
        if (tintIndex == 10)
            return getColor(stack.getMetadata());
        else return 0xFFFFFFFF;
    }

    default int getColor(int id){
        if (id >= JMod.proxy.getMaterialRegistry().size()) return 0xFFFFFFFF;

        return JMod.proxy.getMaterialRegistry().toList().get(id).getColor();
    }
}
