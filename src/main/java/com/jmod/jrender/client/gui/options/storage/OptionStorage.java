package com.jmod.jrender.client.gui.options.storage;

public interface OptionStorage<T> {
    T getData();

    void save();
}
