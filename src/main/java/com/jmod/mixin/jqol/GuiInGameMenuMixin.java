package com.jmod.mixin.jqol;

import com.jmod.jqol.settings.JGuiOptions;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiIngameMenu.class)
public class GuiInGameMenuMixin {
    @Redirect(method = "actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V",
        at = @At(
                value = "NEW",
                target = "Lnet/minecraft/client/gui/GuiOptions;"
        )
    )
    private GuiOptions createGuiOptions(GuiScreen guiScreen, GameSettings settings){
        return new JGuiOptions(guiScreen, settings);
    }
}
