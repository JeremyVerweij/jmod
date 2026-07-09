package com.jmod.jui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.jspecify.annotations.NonNull;

public class JUICustomButton extends GuiButton {
    public JUICustomButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public JUICustomButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(@NonNull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int hoverState = this.getHoverState(this.hovered);
            int halfWidth = this.width / 2;

            this.fixState(mc);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + hoverState * 20, halfWidth, this.height);
            this.drawTexturedModalRect(this.x + halfWidth, this.y, 200 - halfWidth, 46 + hoverState * 20, halfWidth, this.height);
            this.mouseDragged(mc, mouseX, mouseY);

            int textColor = 0x00E0E0E0;

            if (packedFGColour != 0)
            {
                textColor = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                textColor = 0x00A0A0A0;
            }
            else if (this.hovered)
            {
                textColor = 0x00FFFFA0;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + halfWidth, this.y + (this.height - 8) / 2, textColor);
        }
    }

    private void fixState(Minecraft mc){
        mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }
}
