package com.jmod.jui.components;

import com.jmod.jui.ui.JUIScreen;
import net.minecraft.client.Minecraft;

public class VerticalViewportComponent extends BaseComponent {
    protected ScrollbarComponent scrollbar;
    protected int yOffset;
    protected int lowestPoint;
    protected BaseComponent smallestChild;

    public VerticalViewportComponent(String id, Minecraft mc) {
        super(id, mc);
    }

    @Override
    protected void drawBackground(int x, int y, boolean isHover) {
        int lowest = Math.max(0, this.lowestPoint - this.height);
        int scbHeight = this.height / Math.max(lowest, 1);
        this.scrollbar.setScrollBarHeight(scbHeight);

        float scbPos = this.scrollbar.getScrollBarPosition();
        scbHeight = this.scrollbar.getScrollBarHeight();
        int step = Math.ceilDiv(scbHeight, lowest);
        this.scrollbar.setStepSize(step);

        this.yOffset = (int) (lowest * scbPos);
    }

    @Override
    protected void drawForeground(int x, int y, boolean isHover) {

    }

    @Override
    public void draw(int left, int top, int mouseX, int mouseY) {
        this.enableScissors(getDummyX() + left, getDummyY() + top, this.width, this.height);
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
            this.scrollbar.setDummyX(this.getDummyX() + this.width - 10);
            this.scrollbar.setDummyY(this.getDummyY());
            this.scrollbar.setHeight(height);
            this.scrollbar.setForegroundColor(this.foregroundColor);
            this.scrollbar.setBackgroundColor(this.backgroundColor);
            this.scrollbar.setHighlightForegroundColor(this.highlightForegroundColor);
            this.scrollbar.setHighlightBackgroundColor(this.highlightBackgroundColor);
            this.scrollbar.setScrollBarHeight(this.height);
            super.addChild(this.scrollbar);
        }
    }

    @Override
    protected void updateChildSize(BaseComponent child) {
        super.updateChildSize(child);

        if (child == this.smallestChild){
            if (child.dummyY + child.height < this.lowestPoint){
                //TODO: recalculate completly
            }else{
                this.lowestPoint = child.dummyY + child.height;
            }
        } else if (child.dummyY + child.height > this.lowestPoint) {
            this.lowestPoint = child.dummyY + child.height;
            this.smallestChild = child;
        }

        this.lowestPoint = Math.max(0, this.lowestPoint);
    }

    @Override
    public void setOwner(JUIScreen owner) {
        super.setOwner(owner);

        this.scrollbar.setOwner(owner);
    }

    @Override
    public int getChildOffsetY(BaseComponent child) {
        if (child == this.scrollbar) return super.getChildOffsetY(child);

        return -this.yOffset + super.getChildOffsetY(child);
    }
}
