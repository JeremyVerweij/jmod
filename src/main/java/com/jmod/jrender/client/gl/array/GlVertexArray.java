package com.jmod.jrender.client.gl.array;

import com.jmod.jrender.client.gl.GlObject;
import com.jmod.jrender.client.gl.func.GlFunctions;
import com.jmod.jrender.client.gl.device.RenderDevice;

/**
 * Provides Vertex Array functionality on supported platforms.
 */
public class GlVertexArray extends GlObject {
    public static final int NULL_ARRAY_ID = 0;

    public GlVertexArray(RenderDevice owner) {
        super(owner);

        if (!GlFunctions.isVertexArraySupported()) {
            throw new UnsupportedOperationException("Vertex arrays are unsupported on this platform");
        }

        this.setHandle(GlFunctions.VERTEX_ARRAY.glGenVertexArrays());
    }
}
