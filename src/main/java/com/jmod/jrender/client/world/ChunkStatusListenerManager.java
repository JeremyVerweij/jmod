package com.jmod.jrender.client.world;

public interface ChunkStatusListenerManager {
    // TODO: allow multiple listeners to be added?
    void setListener(ChunkStatusListener listener);
}
