package com.jmod.jrender.client.render.world.chunk;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;

import static org.lwjglx.opengl.GL11.*;

public class JRenderChunkRenderContainer extends ChunkRenderContainer {
    public JRenderChunkRenderContainer() {
    }

    public void renderChunkLayer(BlockRenderLayer layer) {
        if (this.initialized) {
            for (RenderChunk renderChunk : this.renderChunks) {
                VertexBuffer vertexBuffer = renderChunk.getVertexBufferByLayer(layer.ordinal());
                GlStateManager.pushMatrix();
                this.preRenderChunk(renderChunk);
                renderChunk.multModelviewMatrix();
                vertexBuffer.bindBuffer();
                this.setupArrayPointers();
                vertexBuffer.drawArrays(7);
                GlStateManager.popMatrix();
            }

            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    private void setupArrayPointers() {
        GlStateManager.glVertexPointer(3, GL_FLOAT, 28, 0); //pos
        GlStateManager.glColorPointer(4, GL_UNSIGNED_BYTE, 28, 12); //color
        GlStateManager.glTexCoordPointer(2, GL_FLOAT, 28, 16); //uv
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.glTexCoordPointer(2, GL_SHORT, 28, 24); //lightmap
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}