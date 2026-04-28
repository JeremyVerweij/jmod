package com.jmod.core.common.utils;

import com.jmod.JMod;

public enum EnergyTier {
    ULV(JMod.MODID + ".energy.tier_ulv", 8),
    LV(JMod.MODID + ".energy.tier_lv", 32),
    MV(JMod.MODID + ".energy.tier_mv", 128),
    HV(JMod.MODID + ".energy.tier_hv", 512),
    EV(JMod.MODID + ".energy.tier_ev", 2_048),
    IV(JMod.MODID + ".energy.tier_iv", 8_192),
    LUV(JMod.MODID + ".energy.tier_luv", 32_768),
    ZPM(JMod.MODID + ".energy.tier_zpm", 131_072),
    UV(JMod.MODID + ".energy.tier_uv", 524_288),
    UHV(JMod.MODID + ".energy.tier_uhv", 2_097_152),
    UEV(JMod.MODID + ".energy.tier_uev", 8_388_608),
    UIV(JMod.MODID + ".energy.tier_uiv", 33_554_432),
    ;

    private final String translationKeyShort;
    private final String translationKeyLong;
    private final long maxVoltage;

    EnergyTier(String translationKey, long maxVoltage){
        this.translationKeyShort = translationKey + ".short";
        this.translationKeyLong = translationKey + ".long";
        this.maxVoltage = maxVoltage;
    }

    public long getMaxVoltage() {
        return maxVoltage;
    }

    public String getTranslationKeyLong() {
        return translationKeyLong;
    }

    public String getTranslationKeyShort() {
        return translationKeyShort;
    }
}
