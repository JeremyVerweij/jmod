package com.jmod.jrender.client.gl.tessellation;

import com.jmod.jrender.client.gl.device.CommandList;

public interface GlTessellation {
    void delete(CommandList commandList);

    void bind(CommandList commandList);

    void unbind(CommandList commandList);

    GlPrimitiveType getPrimitiveType();
}
