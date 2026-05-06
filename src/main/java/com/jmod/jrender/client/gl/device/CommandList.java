package com.jmod.jrender.client.gl.device;

import com.jmod.jrender.client.gl.array.GlVertexArray;
import com.jmod.jrender.client.gl.buffer.*;
import com.jmod.jrender.client.gl.tessellation.GlPrimitiveType;
import com.jmod.jrender.client.gl.tessellation.GlTessellation;
import com.jmod.jrender.client.gl.tessellation.TessellationBinding;

import java.nio.ByteBuffer;

public interface CommandList extends AutoCloseable {
    GlVertexArray createVertexArray();

    GlMutableBuffer createMutableBuffer(GlBufferUsage usage);

    GlTessellation createTessellation(GlPrimitiveType primitiveType, TessellationBinding[] bindings);

    void bindVertexArray(GlVertexArray array);

    default void uploadData(GlMutableBuffer glBuffer, VertexData data) {
        this.uploadData(glBuffer, data.buffer);
    }

    void uploadData(GlMutableBuffer glBuffer, ByteBuffer byteBuffer);

    void copyBufferSubData(GlBuffer src, GlMutableBuffer dst, long readOffset, long writeOffset, long bytes);

    void bindBuffer(GlBufferTarget target, GlBuffer buffer);

    void unbindBuffer(GlBufferTarget target);

    void unbindVertexArray();

    void invalidateBuffer(GlMutableBuffer glBuffer);

    void allocateBuffer(GlBufferTarget target, GlMutableBuffer buffer, long bufferSize);

    void deleteBuffer(GlBuffer buffer);

    void deleteVertexArray(GlVertexArray vertexArray);

    void flush();

    DrawCommandList beginTessellating(GlTessellation tessellation);

    void deleteTessellation(GlTessellation tessellation);

    @Override
    default void close() {
        this.flush();
    }
}
