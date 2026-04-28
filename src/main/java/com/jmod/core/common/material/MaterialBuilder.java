package com.jmod.core.common.material;

import com.jmod.core.common.utils.MiningTier;

public class MaterialBuilder {
    private final Material material;

    public MaterialBuilder(int color, String translationKey){
        this.material = new Material(color, translationKey);
    }

    public Material build(){
        return this.material;
    }

    public MaterialBuilder enableTools(boolean has_heads, boolean hasElectric, boolean hasExpanded, float miningSpeed, int durability, MiningTier toolTier){
        this.material.setProperty(MaterialProperties.HAS_TOOLS, true);
        this.material.setProperty(MaterialProperties.HAS_EXPANDED_TOOLS, hasExpanded);
        this.material.setProperty(MaterialProperties.HAS_ELECTRIC_TOOLS_AND_TOOL_HEADS, hasElectric);
        this.material.setProperty(MaterialProperties.HAS_TOOL_HEADS, has_heads);
        this.material.setToolMiningSpeed(miningSpeed);
        this.material.setToolDurability(durability);
        this.material.setToolTier(toolTier);

        return this;
    }

    public MaterialBuilder enableIngot(){
        this.material.setProperty(MaterialProperties.HAS_INGOT, true);
        return this;
    }

    public MaterialBuilder enableDust(){
        this.material.setProperty(MaterialProperties.HAS_DUST, true);
        return this;
    }

    public MaterialBuilder enableOre(){
        this.material.setProperty(MaterialProperties.HAS_ORE, true);
        return this;
    }

    public MaterialBuilder enableCompression(){
        this.material.setProperty(MaterialProperties.CAN_COMPRESS, true);
        return this;
    }

    public MaterialBuilder enableMagnetic(){
        this.material.setProperty(MaterialProperties.IS_MAGNETIC, true);
        return this;
    }

    public MaterialBuilder enableMolten(int meltingPoint){
        this.material.setProperty(MaterialProperties.IS_MOLTEN, true);
        this.material.setMeltingPoint(meltingPoint);
        return this;
    }

    public MaterialBuilder enableFluid(){
        this.material.setProperty(MaterialProperties.IS_FLUID, true);
        return this;
    }

    public MaterialBuilder enableGas(int boilingPoint){
        this.material.setProperty(MaterialProperties.IS_GAS, true);
        this.material.setBoilingPoint(boilingPoint);
        return this;
    }

    public MaterialBuilder enablePlasma(int plasmaPoint){
        this.material.setProperty(MaterialProperties.IS_PLASMA, true);
        this.material.setPlasmaPoint(plasmaPoint);
        return this;
    }
}
