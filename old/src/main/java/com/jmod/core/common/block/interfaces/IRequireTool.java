package com.jmod.core.common.block.interfaces;

import com.jmod.core.common.item.ToolType;
import com.jmod.core.common.item.material.MetaMaterialToolItem;
import com.jmod.core.common.utils.MiningTier;
import net.minecraft.item.ItemStack;

public interface IRequireTool {
    MiningTier toolLevel();
    ToolType toolType();

    default boolean isToolEffective(ItemStack itemStack){
        if (itemStack.getItem() instanceof MetaMaterialToolItem toolItem){
            return toolItem.getToolType() == this.toolType() &&
                    toolItem.getToolTier(itemStack).ordinal() >= this.toolLevel().ordinal();
        }

        return false;
    }
}
