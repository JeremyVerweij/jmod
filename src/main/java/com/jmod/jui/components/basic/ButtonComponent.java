package com.jmod.jui.components.basic;

import com.jmod.jui.ui.UIDocument;
import com.jmod.jui.ui.interfaces.DoubleInputConsumer;
import net.minecraft.client.Minecraft;

import java.util.Objects;

public class ButtonComponent extends LabelComponent{
    protected boolean useNineSplicedTexture = false;
    protected int borderColor = 0;
    protected int borderColorHover = 0;
    protected int focusBorderColor = 0;
    protected int focusBackColor = 0;
    protected DoubleInputConsumer<ButtonComponent, Integer> onClickEvent = null;

    public ButtonComponent(String id, Minecraft mc) {
        super(id, mc);
        this.centerText = true;
        this.enableHighlight = true;
    }

    @Override
    protected void drawBackground(int x, int y, boolean isHover) {
        if (this.backgroundSprite != null){
            if (this.useNineSplicedTexture){
                //TODO: do stuff
            }else{
                //TODO: do stuff
            }
        }else{
            int backColor = this.backgroundColor;
            int borderColor = this.borderColor;

            if (isHover && this.enableHighlight) backColor = this.highlightBackgroundColor;
            if (this.hasFocus() && this.enableFocusColor) backColor = this.focusBackColor;

            if (isHover && this.enableHighlight) borderColor = this.borderColorHover;
            if (this.hasFocus() && this.enableFocusColor) borderColor = this.focusBorderColor;

            this.drawRect(x, y, x + this.width, y + this.height, backColor);

            this.drawHorizontalLine(x, x + this.width, y, borderColor);
            this.drawHorizontalLine(x, x + this.width, y + this.height, borderColor);
            this.drawVerticalLine(x, y, y + this.height, borderColor);
            this.drawVerticalLine(x + this.width, y, y + this.height, borderColor);
        }
    }

    @Override
    public void onMouseClick(int button, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.onMouseClick(button, offsetX, offsetY, mouseX, mouseY);

        if (this.onClickEvent != null){
            this.onClickEvent.apply(this, button);
        }
    }

    @Override
    public void addExtraAttribute(String key, String value) {
        super.addExtraAttribute(key, value);

        if (Objects.equals(key, "useNineSplice")){
            this.useNineSplicedTexture = Boolean.parseBoolean(value);
        } else if (Objects.equals(key, "borderColorHover")) {
            this.borderColorHover = UIDocument.parseColor(value);
        } else if (Objects.equals(key, "focusBorderColor")) {
            this.focusBorderColor = UIDocument.parseColor(value);
        } else if (Objects.equals(key, "focusFrontColor")) {
            this.focusFrontColor = UIDocument.parseColor(value);
        } else if (Objects.equals(key, "focusBackColor")) {
            this.focusBackColor = UIDocument.parseColor(value);
        } else if (Objects.equals(key, "borderColor")) {
            this.borderColor = UIDocument.parseColor(value);
        }
    }

    public void setOnClickEvent(DoubleInputConsumer<ButtonComponent, Integer> onClickEvent) {
        this.onClickEvent = onClickEvent;
    }
}
