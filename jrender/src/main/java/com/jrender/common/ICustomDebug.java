package com.jrender.common;

import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;

public interface ICustomDebug {
    void addToDebug(List<String> lines, IExtendedBlockState extendedState);
}
