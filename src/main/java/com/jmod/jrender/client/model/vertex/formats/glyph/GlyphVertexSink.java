package com.jmod.jrender.client.model.vertex.formats.glyph;

import com.jmod.jrender.client.model.vertex.VertexSink;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.joml.Matrix4f;

import static com.jmod.jrender.client.util.math.MatrixUtil.*;

public interface GlyphVertexSink extends VertexSink {
    VertexFormat VERTEX_FORMAT = DefaultVertexFormats.POSITION_TEX_LMAP_COLOR;

    /**
     * Writes a glyph vertex to the sink.
     *
     * @param matrix The transformation matrix to apply to the vertex's position
     * @see GlyphVertexSink#writeGlyph(float, float, float, int, float, float, int)
     */
    default void writeGlyph(Matrix4f matrix, float x, float y, float z, int color, float u, float v, int light) {
        float x2 = transformVecX(matrix, x, y, z);
        float y2 = transformVecY(matrix, x, y, z);
        float z2 = transformVecZ(matrix, x, y, z);

        this.writeGlyph(x2, y2, z2, color, u, v, light);
    }

    /**
     * Writes a glyph vertex to the sink.
     *
     * @param x The x-position of the vertex
     * @param y The y-position of the vertex
     * @param z The z-position of the vertex
     * @param color The ABGR-packed color of the vertex
     * @param u The u-texture of the vertex
     * @param v The v-texture of the vertex
     * @param light The packed light map texture coordinates of the vertex
     */
    void writeGlyph(float x, float y, float z, int color, float u, float v, int light);
}
