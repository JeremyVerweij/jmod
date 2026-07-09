package com.jmod.jui.components;

import com.jmod.jui.ui.UIDocument;
import com.jmod.jui.ui.interfaces.DoubleInputConsumer;
import net.minecraft.client.Minecraft;

import java.util.Objects;

public class ButtonComponent extends LabelComponent{
    protected boolean useNineSplicedTexture = false;
    protected int borderColor = 0;
    protected DoubleInputConsumer<ButtonComponent, Integer> onClickEvent = null;

    public ButtonComponent(String id, Minecraft mc) {
        super(id, mc);
        this.centerText = true;
        this.enableHighlight = true;
    }

    @Override
    protected void drawBackground(int left, int top, boolean isHover) {
        int x = left + this.getX();
        int y = top + this.getY();

        if (this.backgroundSprite != null){
            if (this.useNineSplicedTexture){
                //TODO: do stuff
            }else{
                //TODO: do stuff
            }
        }else{
            this.drawRect(x, y, x + this.width, y + this.height, isHover ? this.highlightBackgroundColor : this.backgroundColor);

            this.drawHorizontalLine(x, x + this.width, y, this.borderColor);
            this.drawHorizontalLine(x, x + this.width, y + this.height, this.borderColor);
            this.drawVerticalLine(x, y, y + this.height, this.borderColor);
            this.drawVerticalLine(x + this.width, y, y + this.height, this.borderColor);
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
        } else if (Objects.equals(key, "borderColor")) {
            this.borderColor = UIDocument.parseColor(value);
        }
    }

    public void setOnClickEvent(DoubleInputConsumer<ButtonComponent, Integer> onClickEvent) {
        this.onClickEvent = onClickEvent;
    }
}
