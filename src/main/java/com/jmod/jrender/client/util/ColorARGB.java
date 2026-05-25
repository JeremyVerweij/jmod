package com.jmod.jrender.client.util;

public class ColorARGB {
    private static final int BIT_MASK = 0xFF;
    private static final byte ALPHA_SHIFT = (byte) 24;
    private static final byte RED_SHIFT = (byte) 16;
    private static final byte GREEN_SHIFT = (byte) 8;
    private static final byte BLUE_SHIFT = (byte) 0;

    public static int unpackAlpha(int packed){
        return ((packed >> ALPHA_SHIFT) & BIT_MASK);
    }

    public static int unpackRed(int packed){
        return ((packed >> RED_SHIFT) & BIT_MASK);
    }

    public static int unpackGreen(int packed){
        return ((packed >> GREEN_SHIFT) & BIT_MASK);
    }

    public static int unpackBlue(int packed){
        return ((packed >> BLUE_SHIFT) & BIT_MASK);
    }
}
