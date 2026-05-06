package com.jmod.core.common.utils;

import com.jmod.JMod;
import com.jmod.jmod.Reference;

public enum EnergyTier {
    ULV(Reference.MOD_ID+ ".energy.tier_ulv", 8),
    LV(Reference.MOD_ID + ".energy.tier_lv", 32),
    MV(Reference.MOD_ID + ".energy.tier_mv", 128),
    HV(Reference.MOD_ID + ".energy.tier_hv", 512),
    EV(Reference.MOD_ID + ".energy.tier_ev", 2_048),
    IV(Reference.MOD_ID + ".energy.tier_iv", 8_192),
    LUV(Reference.MOD_ID + ".energy.tier_luv", 32_768),
    ZPM(Reference.MOD_ID + ".energy.tier_zpm", 131_072),
    UV(Reference.MOD_ID + ".energy.tier_uv", 524_288),
    UHV(Reference.MOD_ID + ".energy.tier_uhv", 2_097_152),
    UEV(Reference.MOD_ID + ".energy.tier_uev", 8_388_608),
    UIV(Reference.MOD_ID + ".energy.tier_uiv", 33_554_432),
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
