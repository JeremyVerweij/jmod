package com.jmod.jui.components;

import com.jmod.jui.ui.JUIScreen;
import com.jmod.jui.ui.interfaces.IOffsetProvider;
import net.minecraft.client.Minecraft;

public class VerticalViewportComponent extends BaseComponent implements IOffsetProvider {
    protected ScrollbarComponent scrollbar;
    protected int yOffset;

    public VerticalViewportComponent(String id, Minecraft mc) {
        super(id, mc);
    }

    @Override
    protected void drawBackground(int left, int top, boolean isHover) {
        int lowest = getLowestPoint();
        int scbHeight = this.height / Math.max(lowest, 1);
        this.scrollbar.setScrollBarHeight(scbHeight);

        float scbPos = this.scrollbar.getScrollBarPosition();
        scbHeight = this.scrollbar.getScrollBarHeight();
        int step = Math.ceilDiv(scbHeight, lowest);
        this.scrollbar.setStepSize(step);

        this.yOffset = (int) (lowest * scbPos);
    }

    @Override
    protected void drawForeground(int left, int top, boolean isHover) {

    }

    @Override
    public void draw(int left, int top, int mouseX, int mouseY) {
        this.enableScissors(getX() + left, getY() + top, this.width, this.height);
        super.draw(left, top, mouseX, mouseY);
        this.disableScissors();
    }

    @Override
    public boolean grabFocus() {
        return false;
    }

    @Override
    public void addExtraAttribute(String key, String value) {
        super.addExtraAttribute(key, value);

        if (this.scrollbar == null){
            this.scrollbar = new ScrollbarComponent(id + "-scrollbar", mc);
            this.scrollbar.setWidth(10);
            this.scrollbar.setX(this.getX() + this.width - 10);
            this.scrollbar.setY(this.getY());
            this.scrollbar.setHeight(height);
            this.scrollbar.setForegroundColor(this.foregroundColor);
            this.scrollbar.setBackgroundColor(this.backgroundColor);
            this.scrollbar.setHighlightForegroundColor(this.highlightForegroundColor);
            this.scrollbar.setHighlightBackgroundColor(this.highlightBackgroundColor);
            this.scrollbar.setScrollBarHeight(this.height);
            super.addChild(this.scrollbar);
        }
    }

    protected int getLowestPoint(){
        int lowest = 0;

        for (BaseComponent child : this.children) {
            if (child.y + child.height > lowest) lowest = child.y + child.height;
        }

        return Math.max(0, lowest - this.height);
    }

    @Override
    public void setOwner(JUIScreen owner) {
        super.setOwner(owner);

        this.scrollbar.setOwner(owner);
    }

    @Override
    public void addChild(BaseComponent component) {
        super.addChild(component);
        component.setOffsetProvider(this);
    }

    @Override
    public int getOffsetX(BaseComponent component) {
        return 0;
    }

    @Override
    public int getOffsetY(BaseComponent component) {
        return -this.yOffset;
    }
}
