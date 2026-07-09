package com.jmod.jui.ui.interfaces;

import com.jmod.jui.components.BaseComponent;
import net.minecraft.client.Minecraft;

public interface ComponentProvider<T extends BaseComponent> {
    T create(String id, Minecraft mc);
}
