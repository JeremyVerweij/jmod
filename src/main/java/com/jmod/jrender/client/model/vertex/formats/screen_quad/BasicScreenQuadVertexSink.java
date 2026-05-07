package com.jmod.jrender.client.model.vertex.formats.screen_quad;

import com.jmod.jrender.client.model.vertex.VertexSink;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.joml.Matrix4f;

import static com.jmod.jrender.client.util.math.MatrixUtil.*;

public interface BasicScreenQuadVertexSink extends VertexSink {
    VertexFormat VERTEX_FORMAT = DefaultVertexFormats.POSITION_COLOR;

    /**
     * Writes a quad vertex to this sink.
     *
     * @param x The x-position of the vertex
     * @param y The y-position of the vertex
     * @param z The z-position of the vertex
     * @param color The ABGR-packed color of the vertex
     */
    void writeQuad(float x, float y, float z, int color);

    /**
     * Writes a quad vertex to the sink, transformed by the given matrix.
     *
     * @param matrix The matrix to transform the vertex's position by
     */
    default void writeQuad(Matrix4f matrix, float x, float y, float z, int color) {
        float x2 = transformVecX(matrix, x, y, z);
        float y2 = transformVecY(matrix, x, y, z);
        float z2 = transformVecZ(matrix, x, y, z);

        this.writeQuad(x2, y2, z2, color);
    }
}
