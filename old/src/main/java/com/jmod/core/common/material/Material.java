package com.jmod.core.common.material;

import com.jmod.core.common.utils.EnergyTier;
import com.jmod.core.common.utils.MiningTier;

import java.util.BitSet;

public class Material {
    private final int color;
    private final String materialTranslationKey;
    private final BitSet properties;
    private EnergyTier cableTier;
    private EnergyTier processingTier;
    private MiningTier miningTier;
    private MiningTier toolTier;
    private short maxAmps;
    private short energyLossWire;
    private short energyLossCable;
    private int fluidThroughput;
    private int itemThroughput;
    private int meltingPoint;
    private int boilingPoint;
    private int plasmaPoint;
    private int toolDurability;
    private float toolMiningSpeed;
    private float armor;
    private float hardness;

    public Material(int color, String materialTranslationKey){
        this.color = color;
        this.materialTranslationKey = materialTranslationKey;
        this.properties = new BitSet(MaterialProperties.values().length);

        this.cableTier = null;
        this.processingTier = null;
        this.miningTier = null;
        this.toolTier = null;
        this.maxAmps = 0;
        this.energyLossWire = 0;
        this.energyLossCable = 0;
        this.fluidThroughput = 0;
        this.itemThroughput = 0;
        this.meltingPoint = 0;
        this.boilingPoint = 0;
        this.plasmaPoint = 0;
        this.toolDurability = 0;
        this.toolMiningSpeed = 0;
        this.hardness = 0;
        this.armor = 0;
    }

    public int getColor() {
        return color;
    }

    public String getMaterialTranslationKey() {
        return materialTranslationKey;
    }

    public boolean getProperty(int index){
        return this.properties.get(index);
    }

    public boolean getProperty(MaterialProperties property){
        return getProperty(property.ordinal());
    }

    public void setProperty(int index, boolean value){
        this.properties.set(index, value);
    }

    public void setProperty(MaterialProperties property, boolean value){
        setProperty(property.ordinal(), value);
    }

    public EnergyTier getCableTier() {
        return cableTier;
    }

    public void setCableTier(EnergyTier cableTier) {
        this.cableTier = cableTier;
    }

    public EnergyTier getProcessingTier() {
        return processingTier;
    }

    public void setProcessingTier(EnergyTier processingTier) {
        this.processingTier = processingTier;
    }

    public short getMaxAmps() {
        return maxAmps;
    }

    public void setMaxAmps(short maxAmps) {
        this.maxAmps = maxAmps;
    }

    public short getEnergyLossWire() {
        return energyLossWire;
    }

    public void setEnergyLossWire(short energyLossWire) {
        this.energyLossWire = energyLossWire;
    }

    public short getEnergyLossCable() {
        return energyLossCable;
    }

    public void setEnergyLossCable(short energyLossCable) {
        this.energyLossCable = energyLossCable;
    }

    public int getFluidThroughput() {
        return fluidThroughput;
    }

    public void setFluidThroughput(int fluidThroughput) {
        this.fluidThroughput = fluidThroughput;
    }

    public int getItemThroughput() {
        return itemThroughput;
    }

    public void setItemThroughput(int itemThroughput) {
        this.itemThroughput = itemThroughput;
    }

    public int getMeltingPoint() {
        return meltingPoint;
    }

    public void setMeltingPoint(int meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

    public int getBoilingPoint() {
        return boilingPoint;
    }

    public void setBoilingPoint(int boilingPoint) {
        this.boilingPoint = boilingPoint;
    }

    public int getToolDurability() {
        return toolDurability;
    }

    public void setToolDurability(int toolDurability) {
        this.toolDurability = toolDurability;
    }

    public float getToolMiningSpeed() {
        return toolMiningSpeed;
    }

    public void setToolMiningSpeed(float toolMiningSpeed) {
        this.toolMiningSpeed = toolMiningSpeed;
    }

    public float getHardness() {
        return hardness;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }

    public MiningTier getMiningTier() {
        return miningTier;
    }

    public void setMiningTier(MiningTier miningTier) {
        this.miningTier = miningTier;
    }

    public MiningTier getToolTier() {
        return toolTier;
    }

    public void setToolTier(MiningTier toolTier) {
        this.toolTier = toolTier;
    }

    public int getPlasmaPoint() {
        return plasmaPoint;
    }

    public void setPlasmaPoint(int plasmaPoint) {
        this.plasmaPoint = plasmaPoint;
    }

    public float getArmor() {
        return armor;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }
}
