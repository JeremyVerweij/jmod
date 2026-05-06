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

    public MaterialBuilder enableArmor(boolean has_parts, float armor, int durability){
        this.material.setProperty(MaterialProperties.HAS_ARMOR, true);
        this.material.setProperty(MaterialProperties.HAS_ARMOR_PARTS, has_parts);
        this.material.setArmor(armor);
        this.material.setToolDurability(durability);

        return this;
    }

    public MaterialBuilder enableFluidPipe(int fluidThroughput){
        this.material.setProperty(MaterialProperties.HAS_FLUID_PIPE, true);
        this.material.setFluidThroughput(fluidThroughput);

        return this;
    }

    public MaterialBuilder enableItemPipe(int itemThroughput){
        this.material.setProperty(MaterialProperties.HAS_ITEM_PIPE, true);
        this.material.setItemThroughput(itemThroughput);

        return this;
    }

    public MaterialBuilder enableWireAndCable(short maxAmps, short energyLossWire, short energyLossCable){
        this.material.setProperty(MaterialProperties.HAS_WIRE_AND_CABLE, true);
        this.material.setMaxAmps(maxAmps);
        this.material.setEnergyLossWire(energyLossWire);
        this.material.setEnergyLossCable(energyLossCable);

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

    public MaterialBuilder enableRotor(){
        this.material.setProperty(MaterialProperties.HAS_ROTOR, true);
        return this;
    }

    public MaterialBuilder enableGear(){
        this.material.setProperty(MaterialProperties.HAS_GEAR, true);
        return this;
    }

    public MaterialBuilder enableSpring(){
        this.material.setProperty(MaterialProperties.HAS_SPRING, true);
        return this;
    }

    public MaterialBuilder enableGem(){
        this.material.setProperty(MaterialProperties.HAS_GEM, true);
        return this;
    }

    public MaterialBuilder enablePlate(){
        this.material.setProperty(MaterialProperties.HAS_PLATE, true);
        return this;
    }

    public MaterialBuilder enableRod(){
        this.material.setProperty(MaterialProperties.HAS_ROD, true);
        return this;
    }

    public MaterialBuilder enableCasing(){
        this.material.setProperty(MaterialProperties.HAS_CASING, true);
        return this;
    }

    public MaterialBuilder enableFrame(){
        this.material.setProperty(MaterialProperties.HAS_FRAME, true);
        return this;
    }

    public MaterialBuilder enableBlock(){
        this.material.setProperty(MaterialProperties.HAS_BLOCK, true);
        return this;
    }

    public MaterialBuilder enableRing(){
        this.material.setProperty(MaterialProperties.HAS_RING, true);
        return this;
    }

    public MaterialBuilder enableScrew(){
        this.material.setProperty(MaterialProperties.HAS_SCREW, true);
        return this;
    }

    public MaterialBuilder enableHotIngot(){
        this.material.setProperty(MaterialProperties.HAS_HOT, true);
        return this;
    }

    public MaterialBuilder enableFluidCell(){
        this.material.setProperty(MaterialProperties.HAS_FLUID_CELL, true);
        return this;
    }

    public MaterialBuilder enableTankWall(){
        this.material.setProperty(MaterialProperties.HAS_TANK_WALL, true);
        return this;
    }

    public MaterialBuilder enableFluidDrum(){
        this.material.setProperty(MaterialProperties.IS_DRUM, true);
        return this;
    }

    public MaterialBuilder enableChest(){
        this.material.setProperty(MaterialProperties.IS_CHEST, true);
        return this;
    }

    public MaterialBuilder enableEnchantmentUpgrade(){
        this.material.setProperty(MaterialProperties.IS_ENCHANTMENT_UPGRADE, true);
        return this;
    }

    public MaterialBuilder enableLens(){
        this.material.setProperty(MaterialProperties.HAS_LENS, true);
        return this;
    }

    public MaterialBuilder enableFoil(){
        this.material.setProperty(MaterialProperties.HAS_FOIL, true);
        return this;
    }

    public MaterialBuilder enableFineWire(){
        this.material.setProperty(MaterialProperties.HAS_FINE_WIRE, true);
        return this;
    }
}
