package com.jmod.mixin.jrender.features.options;

import com.jmod.jrender.JRender;
import com.jmod.jrender.client.gui.SodiumGameOptions;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {
    /**
     * @author JellySquid
     * @reason Make ambient occlusion user configurable
     */
    @Overwrite
    public static boolean isAmbientOcclusionEnabled() {
        return JRender.options().quality.smoothLighting != SodiumGameOptions.LightingQuality.OFF;
    }
}
