package com.jmod.jrender.client.gl.buffer;

import com.jmod.jrender.client.gl.GlObject;
import com.jmod.jrender.client.gl.device.RenderDevice;
import org.lwjgl.opengl.GL15;

public abstract class GlBuffer extends GlObject {
    public static final int NULL_BUFFER_ID = 0;

    protected final GlBufferUsage usage;

    protected GlBuffer(RenderDevice owner, GlBufferUsage usage) {
        super(owner);

        this.setHandle(GL15.glGenBuffers());

        this.usage = usage;
    }

    public GlBufferUsage getUsageHint() {
        return this.usage;
    }
}
