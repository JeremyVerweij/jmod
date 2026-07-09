package com.jmod.jui.ui;

import com.jmod.jui.components.BaseComponent;
import com.jmod.jui.ui.interfaces.ComponentProvider;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

public class UIComponentCollection {
    private static final UIComponentCollection instance = new UIComponentCollection();

    public static UIComponentCollection getInstance(){
        return instance;
    }

    public static void addComponent(String name, ComponentProvider<?> componentProvider){
        getInstance().components.put(name, componentProvider);
    }

    public static BaseComponent createNewComponent(String name, String id, Minecraft mc){
        if (!getInstance().components.containsKey(name)) return getInstance().components.get("undefined").create(id, mc);

        return getInstance().components.get(name).create(id, mc);
    }

    private final Map<String, ComponentProvider<?>> components;

    private UIComponentCollection(){
        this.components = new HashMap<>();
    }
}
