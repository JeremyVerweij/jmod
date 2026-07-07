package com.jmod.core.common.event;

import com.jmod.core.common.block.MetaBlock;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;

public class MetaBlockRegisterEvent extends Event {
    private final List<MetaBlock> REGISTRY;

    public MetaBlockRegisterEvent(){
        this.REGISTRY = new ArrayList<>();
    }

    public List<MetaBlock> getRegistry() {
        return REGISTRY;
    }

    public void register(MetaBlock metaBlock){
        this.REGISTRY.add(metaBlock);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
