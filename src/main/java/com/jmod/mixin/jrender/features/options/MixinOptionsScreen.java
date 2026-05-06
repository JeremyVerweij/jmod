package com.jmod.mixin.jrender.features.options;

import com.jmod.jrender.client.gui.SodiumOptionsGUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptions.class)
public class MixinOptionsScreen extends GuiScreen {

    @Dynamic
    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void open(GuiButton button, CallbackInfo ci) {
        if(button.enabled && button.id == 101) {
            this.mc.displayGuiScreen(new SodiumOptionsGUI(this));

            ci.cancel();
        }
    }
}
