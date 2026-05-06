package com.jmod.jrender.client.gui.utils;

public interface Element {
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    default boolean mouseDragged(double mouseX, double mouseY) {
        return false;
    }
}