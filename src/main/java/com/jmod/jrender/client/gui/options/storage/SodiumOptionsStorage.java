package com.jmod.jrender.client.gui.options.storage;

import com.jmod.jrender.JRender;
import com.jmod.jrender.client.gui.SodiumGameOptions;

import java.io.IOException;

public class SodiumOptionsStorage implements OptionStorage<SodiumGameOptions> {
    private final SodiumGameOptions options;

    public SodiumOptionsStorage() {
        this.options = JRender.options();
    }

    @Override
    public SodiumGameOptions getData() {
        return this.options;
    }

    @Override
    public void save() {
        try {
            this.options.writeChanges();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save configuration changes", e);
        }

        JRender.logger().info("Flushed changes to " + JRender.NAME + " configuration");
    }
}
