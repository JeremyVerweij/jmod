package com.jmod.core.common.utils.unlisterProperty;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyByte implements IUnlistedProperty<Byte> {
    private final String name;
    private final byte min, max;

    public UnlistedPropertyByte(String name, byte min, byte max){
        this.name = name;
        this.min = min;
        this.max = max;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isValid(Byte value) {
        return this.min <= value && this.max >= value;
    }

    @Override
    public Class<Byte> getType() {
        return Byte.class;
    }

    @Override
    public String valueToString(Byte value) {
        return value.toString();
    }
}
