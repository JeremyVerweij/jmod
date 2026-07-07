package com.jmod.core.common.utils;

import com.jmod.Tags;

public enum EnergyTier {
    ULV(Tags.MOD_ID + ".energy.tier_ulv", 8),
    LV(Tags.MOD_ID + ".energy.tier_lv", 32),
    MV(Tags.MOD_ID + ".energy.tier_mv", 128),
    HV(Tags.MOD_ID + ".energy.tier_hv", 512),
    EV(Tags.MOD_ID + ".energy.tier_ev", 2_048),
    IV(Tags.MOD_ID + ".energy.tier_iv", 8_192),
    LUV(Tags.MOD_ID + ".energy.tier_luv", 32_768),
    ZPM(Tags.MOD_ID + ".energy.tier_zpm", 131_072),
    UV(Tags.MOD_ID + ".energy.tier_uv", 524_288),
    UHV(Tags.MOD_ID + ".energy.tier_uhv", 2_097_152),
    UEV(Tags.MOD_ID + ".energy.tier_uev", 8_388_608),
    UIV(Tags.MOD_ID + ".energy.tier_uiv", 33_554_432),
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
