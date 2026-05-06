package com.jmod.mixin.jrender.features.options;

import com.jmod.jrender.JRender;
import com.jmod.jrender.client.gui.SodiumGameOptions;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameSettings.class)
public class MixinGameOptions {
    @Shadow
    public int renderDistanceChunks;

    @Shadow
    public boolean fancyGraphics;

    /**
     * @author Asek3
     * @reason Implemented cloud rendering option
     */
    @Overwrite
    public int shouldRenderClouds() {
        SodiumGameOptions options = JRender.options();

        if (this.renderDistanceChunks < 4 || !options.quality.enableClouds) {
            return 0;
        }

        return options.quality.cloudQuality.isFancy(this.fancyGraphics) ? 2 : 1;
    }
}