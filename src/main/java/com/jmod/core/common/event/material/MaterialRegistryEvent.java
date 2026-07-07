package com.jmod.core.common.event.material;

import com.jmod.core.common.material.MaterialRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MaterialRegistryEvent extends Event {
    public final MaterialRegistry REGISTRY;

    public MaterialRegistryEvent(MaterialRegistry registry){
        this.REGISTRY = registry;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
