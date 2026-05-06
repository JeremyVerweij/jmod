package com.jmod.jrender.client.render.chunk.backends.oneshot;

import com.jmod.jrender.client.gl.attribute.GlVertexAttributeBinding;
import com.jmod.jrender.client.gl.attribute.GlVertexFormat;
import com.jmod.jrender.client.gl.buffer.GlBufferUsage;
import com.jmod.jrender.client.gl.buffer.GlMutableBuffer;
import com.jmod.jrender.client.gl.buffer.VertexData;
import com.jmod.jrender.client.gl.device.CommandList;
import com.jmod.jrender.client.gl.device.RenderDevice;
import com.jmod.jrender.client.gl.tessellation.GlPrimitiveType;
import com.jmod.jrender.client.gl.tessellation.GlTessellation;
import com.jmod.jrender.client.gl.tessellation.TessellationBinding;
import com.jmod.jrender.client.gl.util.BufferSlice;
import com.jmod.jrender.client.model.quad.properties.ModelQuadFacing;
import com.jmod.jrender.client.render.chunk.ChunkGraphicsState;
import com.jmod.jrender.client.render.chunk.ChunkRenderContainer;
import com.jmod.jrender.client.render.chunk.data.ChunkMeshData;
import com.jmod.jrender.client.render.chunk.format.ChunkMeshAttribute;
import com.jmod.jrender.client.render.chunk.shader.ChunkShaderBindingPoints;

import java.util.Arrays;
import java.util.Map;

public class ChunkOneshotGraphicsState extends ChunkGraphicsState {
    private final GlMutableBuffer vertexBuffer;

    protected GlTessellation tessellation;

    private final long[] parts;

    protected ChunkOneshotGraphicsState(RenderDevice device, ChunkRenderContainer<?> container) {
        super(container);

        this.parts = new long[ModelQuadFacing.COUNT];

        try (CommandList commands = device.createCommandList()) {
            this.vertexBuffer = commands.createMutableBuffer(GlBufferUsage.GL_STATIC_DRAW);
        }
    }

    public long getModelPart(int facing) {
        return this.parts[facing];
    }

    protected void setModelPart(ModelQuadFacing facing, long slice) {
        this.parts[facing.ordinal()] = slice;
    }

    protected void setupModelParts(ChunkMeshData meshData, GlVertexFormat<?> vertexFormat) {
        int stride = vertexFormat.getStride();

        Arrays.fill(this.parts, 0L);

        for (Map.Entry<ModelQuadFacing, BufferSlice> entry : meshData.getSlices()) {
            ModelQuadFacing facing = entry.getKey();
            BufferSlice slice = entry.getValue();

            this.setModelPart(facing, BufferSlice.pack(slice.start / stride, slice.len / stride));
        }
    }

    @Override
    public void delete(CommandList commandList) {
        commandList.deleteBuffer(this.vertexBuffer);
    }

    public void upload(CommandList commandList, ChunkMeshData meshData) {
        VertexData vertexData = meshData.takeVertexData();

        commandList.uploadData(this.vertexBuffer, vertexData);

        GlVertexFormat<ChunkMeshAttribute> vertexFormat = (GlVertexFormat<ChunkMeshAttribute>) vertexData.format;

        this.tessellation = commandList.createTessellation(GlPrimitiveType.QUADS, new TessellationBinding[] {
                new TessellationBinding(this.vertexBuffer, new GlVertexAttributeBinding[] {
                        new GlVertexAttributeBinding(ChunkShaderBindingPoints.POSITION, vertexFormat.getAttribute(ChunkMeshAttribute.POSITION)),
                        new GlVertexAttributeBinding(ChunkShaderBindingPoints.COLOR, vertexFormat.getAttribute(ChunkMeshAttribute.COLOR)),
                        new GlVertexAttributeBinding(ChunkShaderBindingPoints.TEX_COORD, vertexFormat.getAttribute(ChunkMeshAttribute.TEXTURE)),
                        new GlVertexAttributeBinding(ChunkShaderBindingPoints.LIGHT_COORD, vertexFormat.getAttribute(ChunkMeshAttribute.LIGHT))
                }, false)
        });

        this.setupModelParts(meshData, vertexData.format);

        vertexData.buffer.limit(vertexData.buffer.capacity());
        vertexData.buffer.position(0);
        this.setTranslucencyData(vertexData.buffer);
    }
}
