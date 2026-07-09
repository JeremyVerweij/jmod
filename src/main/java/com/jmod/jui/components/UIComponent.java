package com.jmod.jui.components;

import net.minecraft.client.Minecraft;

public class UIComponent extends BaseComponent{
    public UIComponent(String id, Minecraft mc) {
        super(id, mc);
    }

    @Override
    protected void drawBackground(int left, int top, boolean isHover) {

    }

    @Override
    protected void drawForeground(int left, int top, boolean isHover) {
        int x = left + this.getX();
        int y = top + this.getY();

        this.drawVerticalLine(x, y, y + this.height, 0xFFFFFFFF);
        this.drawVerticalLine(x + this.width, y, y + this.height, 0xFFFFFFFF);
        this.drawHorizontalLine(x, x + this.width, y, 0xFFFFFFFF);
        this.drawHorizontalLine(x, x + this.width, y + this.height, 0xFFFFFFFF);
    }

    @Override
    public boolean grabFocus() {
        return false;
    }
}
