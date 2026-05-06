package com.jmod.jrender.client.model.vertex;

import com.jmod.jrender.client.model.vertex.formats.glyph.GlyphVertexSink;
import com.jmod.jrender.client.model.vertex.formats.glyph.GlyphVertexType;
import com.jmod.jrender.client.model.vertex.formats.line.LineVertexSink;
import com.jmod.jrender.client.model.vertex.formats.line.LineVertexType;
import com.jmod.jrender.client.model.vertex.formats.particle.ParticleVertexSink;
import com.jmod.jrender.client.model.vertex.formats.particle.ParticleVertexType;
import com.jmod.jrender.client.model.vertex.formats.quad.QuadVertexSink;
import com.jmod.jrender.client.model.vertex.formats.quad.QuadVertexType;
import com.jmod.jrender.client.model.vertex.formats.screen_quad.BasicScreenQuadVertexSink;
import com.jmod.jrender.client.model.vertex.formats.screen_quad.BasicScreenQuadVertexType;
import com.jmod.jrender.client.model.vertex.type.VanillaVertexType;

public class VanillaVertexTypes {
    public static final VanillaVertexType<QuadVertexSink> QUADS = new QuadVertexType();
    public static final VanillaVertexType<LineVertexSink> LINES = new LineVertexType();
    public static final VanillaVertexType<GlyphVertexSink> GLYPHS = new GlyphVertexType();
    public static final VanillaVertexType<ParticleVertexSink> PARTICLES = new ParticleVertexType();
    public static final VanillaVertexType<BasicScreenQuadVertexSink> BASIC_SCREEN_QUADS = new BasicScreenQuadVertexType();
}
