package com.jmod.jui.components.basic;

import com.jmod.jui.components.BaseComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.Objects;

public class LabelComponent extends BaseComponent {
    protected boolean centerText = false;
    protected boolean enableHighlight = false;
    protected boolean enableFocusColor = false;
    protected int focusFrontColor = 0;

    public LabelComponent(String id, Minecraft mc) {
        super(id, mc);
    }

    @Override
    protected void drawBackground(int x, int y, boolean isHover) {

    }

    @Override
    protected void drawForeground(int x, int y, boolean isHover) {
        int frontColor = this.foregroundColor;

        if (isHover && this.enableHighlight) frontColor = this.highlightForegroundColor;
        if (this.hasFocus() && this.enableFocusColor) frontColor = this.focusFrontColor;

        if (this.height == 0)
            this.height = this.mc.fontRenderer.FONT_HEIGHT;

        if (centerText){
            this.drawCenteredString(this.mc.fontRenderer, this.getTranslatedText(),
                    x + (this.width / 2) + textXOffset(), y + (this.height - this.mc.fontRenderer.FONT_HEIGHT) / 2,
                    frontColor);
        }else{
            this.drawString(this.mc.fontRenderer, this.getTranslatedText(),
                    x + textXOffset(), y + (this.height - this.mc.fontRenderer.FONT_HEIGHT) / 2,
                    frontColor);
        }
    }

    @Override
    public boolean grabFocus() {
        return false;
    }

    @Override
    public void addExtraAttribute(String key, String value) {
        super.addExtraAttribute(key, value);

        if (Objects.equals(key, "textCenter")){
            this.centerText = Boolean.parseBoolean(value);
        } else if (Objects.equals(key, "enableHighlight")) {
            this.enableHighlight = Boolean.parseBoolean(value);
        }
    }

    protected String getTranslatedText(){
        return I18n.format(this.translatorProvider != null ? this.translatorProvider.getTranslationKey() : this.translationKey);
    }

    protected int textXOffset(){
        return 0;
    }
}
