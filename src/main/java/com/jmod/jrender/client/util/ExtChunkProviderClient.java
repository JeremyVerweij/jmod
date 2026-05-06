package com.jmod.jrender.client.util;

public interface ExtChunkProviderClient {

    boolean needsTrackingUpdate();

    void setNeedsTrackingUpdate(boolean state);
}