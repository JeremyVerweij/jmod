package com.jmod.core.common.block;

import com.jmod.JMod;
import com.jmod.core.common.block.interfaces.IMaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public abstract class MetaMaterialBlock extends MetaBlock implements IMaterialColor {
    public MetaMaterialBlock(String modId, String registryName, Material blockMaterialIn) {
        super(modId, registryName, blockMaterialIn, (short) JMod.proxy.getMaterialRegistry().size());
    }

    public MetaMaterialBlock(String modId, String registryName, Material blockMaterialIn, CreativeTabs creativeTab) {
        super(modId, registryName, blockMaterialIn, creativeTab, (short) JMod.proxy.getMaterialRegistry().size());
    }
}
