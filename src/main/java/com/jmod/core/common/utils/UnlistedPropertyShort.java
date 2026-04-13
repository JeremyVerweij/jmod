package com.jmod.core.common.utils;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyShort implements IUnlistedProperty<Short> {
    private final String name;
    private final short min, max;

    public UnlistedPropertyShort(String name, short min, short max){
        this.name = name;
        this.min = min;
        this.max = max;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isValid(Short value) {
        return this.min <= value && this.max >= value;
    }

    @Override
    public Class<Short> getType() {
        return Short.class;
    }

    @Override
    public String valueToString(Short value) {
        return value.toString();
    }
}
