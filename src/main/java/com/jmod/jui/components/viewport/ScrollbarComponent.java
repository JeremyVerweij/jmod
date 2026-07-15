package com.jmod.jui.components.viewport;

import com.jmod.jui.components.BaseComponent;
import net.minecraft.client.Minecraft;

import java.util.Objects;

public class ScrollbarComponent extends BaseComponent {
    protected boolean horizontal = false;
    protected int scrollBarPosition = 0;
    protected int scrollBarHeight = 0;
    protected int stepSize = 0;

    public ScrollbarComponent(String id, Minecraft mc) {
        super(id, mc);
    }

    @Override
    protected void drawBackground(int x, int y, boolean isHover) {
        this.drawRect(x, y, x + this.width, y + this.height, this.backgroundColor);
    }

    @Override
    protected void drawForeground(int x, int y, boolean isHover) {
        this.drawRect(x, y + this.scrollBarPosition, x + this.width,
                y + this.scrollBarPosition + this.scrollBarHeight, this.foregroundColor);
    }

    @Override
    public boolean grabFocus() {
        return false;
    }

    @Override
    public void onMouseScroll(int amount, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.onMouseScroll(amount, offsetX, offsetY, mouseX, mouseY);

        this.scrollBarPosition = Math.clamp(this.scrollBarPosition - (amount * stepSize),
                0, this.height - this.scrollBarHeight);
    }

    @Override
    public void onMouseDrag(int dragX, int dragY, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.onMouseDrag(dragX, dragY, offsetX, offsetY, mouseX, mouseY);

        this.scrollBarPosition = Math.clamp(this.scrollBarPosition - (dragY * stepSize),
                0, this.height - this.scrollBarHeight);
    }

    @Override
    public void onMouseClick(int button, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.onMouseClick(button, offsetX, offsetY, mouseX, mouseY);

        if (button != 0) return;

        this.scrollBarPosition = Math.clamp(mouseY - (this.scrollBarHeight / 2),
                0, this.height - this.scrollBarHeight);

        this.owner.setDragging(this);
    }

    @Override
    public void addExtraAttribute(String key, String value) {
        super.addExtraAttribute(key, value);

        if (Objects.equals(key, "horizontal")){
            this.horizontal = Boolean.parseBoolean(value);
        } else if (Objects.equals(key, "scbHeight")) {
            this.scrollBarHeight = Integer.parseInt(value);
        }
    }

    public void setScrollBarHeight(int scrollBarHeight) {
        this.scrollBarHeight = Math.clamp(scrollBarHeight, 100, this.height);
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public float getScrollBarPosition() {
        return ((float) scrollBarPosition / (float) (this.height - this.scrollBarHeight));
    }

    public int getScrollBarHeight() {
        return scrollBarHeight;
    }
}
