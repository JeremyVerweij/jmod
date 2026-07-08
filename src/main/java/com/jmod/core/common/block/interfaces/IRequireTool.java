package com.jmod.core.common.block.interfaces;

import com.jmod.core.common.item.ToolType;
import com.jmod.core.common.item.interfaces.IToolItem;
import com.jmod.core.common.utils.MiningTier;
import net.minecraft.item.ItemStack;

import java.util.Set;

public interface IRequireTool {
    MiningTier toolLevel();
    Set<ToolType > toolType();

    default boolean isToolEffective(ItemStack itemStack){
        if (itemStack.getItem() instanceof IToolItem toolItem){
            return this.toolType().contains(toolItem.getToolType()) &&
                    toolItem.getToolTier(itemStack).ordinal() >= this.toolLevel().ordinal();
        }

        return false;
    }
}
