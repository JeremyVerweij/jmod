package com.jmod.core.common.event;

import com.jmod.core.common.item.MetaItem;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;

public class MetaItemRegisterEvent extends Event {
    private final List<MetaItem> REGISTRY;

    public MetaItemRegisterEvent(){
        this.REGISTRY = new ArrayList<>();
    }

    public List<MetaItem> getRegistry() {
        return REGISTRY;
    }

    public void register(MetaItem metaItem){
        this.REGISTRY.add(metaItem);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
