package com.jmod.mixin.jui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui {
    @Shadow
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    @Shadow
    public abstract void drawWorldBackground(int tint);

    @Shadow
    protected FontRenderer fontRenderer;

    @Shadow
    public int height;
    @Shadow
    public int width;

    /**
     * @author jmod
     * @reason debugging
     */
    @Overwrite
    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent((GuiScreen) (Object) this));
        this.drawString(this.fontRenderer, "W: " + this.width + ", H: " + this.height, 0, 0, 16777215);
    }
}
