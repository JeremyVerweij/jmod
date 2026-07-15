package com.jmod.jui.components.basic;

import com.jmod.jui.components.BaseComponent;
import net.minecraft.client.Minecraft;

public class UndefinedComponent extends BaseComponent {
    public UndefinedComponent(String id, Minecraft mc) {
        super(id, mc);
    }

    @Override
    protected void drawBackground(int x, int y, boolean isHover) {

    }

    @Override
    protected void drawForeground(int x, int y, boolean isHover) {

    }

    @Override
    public boolean grabFocus() {
        return false;
    }
}
