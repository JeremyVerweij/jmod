package com.jmod.jrender.client.render.chunk.shader;

import com.jmod.jrender.client.gl.attribute.GlVertexFormat;
import com.jmod.jrender.client.gl.shader.GlProgram;
import com.jmod.jrender.client.gl.shader.GlShader;
import com.jmod.jrender.client.gl.device.RenderDevice;
import com.jmod.jrender.client.gl.shader.ShaderLoader;
import com.jmod.jrender.client.gl.shader.ShaderType;
import com.jmod.jrender.client.gl.compat.FogHelper;
import com.jmod.jrender.client.model.vertex.type.ChunkVertexType;
import com.jmod.jrender.client.render.chunk.ChunkGraphicsState;
import com.jmod.jrender.client.render.chunk.ChunkRenderBackend;
import com.jmod.jrender.client.render.chunk.format.ChunkMeshAttribute;
import net.minecraft.util.ResourceLocation;

import java.util.EnumMap;

public abstract class ChunkRenderShaderBackend<T extends ChunkGraphicsState>
        implements ChunkRenderBackend<T> {
    private final EnumMap<ChunkFogMode, ChunkProgram> programs = new EnumMap<>(ChunkFogMode.class);

    protected final ChunkVertexType vertexType;
    protected final GlVertexFormat<ChunkMeshAttribute> vertexFormat;

    protected ChunkProgram activeProgram;

    public ChunkRenderShaderBackend(ChunkVertexType vertexType) {
        this.vertexType = vertexType;
        this.vertexFormat = vertexType.getCustomVertexFormat();
    }

    private ChunkProgram createShader(RenderDevice device, ChunkFogMode fogMode, GlVertexFormat<ChunkMeshAttribute> vertexFormat) {
        GlShader vertShader = ShaderLoader.loadShader(device, ShaderType.VERTEX,
                new ResourceLocation("jrender", "chunk_gl20.v.glsl"), fogMode.getDefines());

        GlShader fragShader = ShaderLoader.loadShader(device, ShaderType.FRAGMENT,
                new ResourceLocation("jrender", "chunk_gl20.f.glsl"), fogMode.getDefines());

        try {
            return GlProgram.builder(new ResourceLocation("relictium", "chunk_shader"))
                    .attachShader(vertShader)
                    .attachShader(fragShader)
                    .bindAttribute("a_Pos", ChunkShaderBindingPoints.POSITION)
                    .bindAttribute("a_Color", ChunkShaderBindingPoints.COLOR)
                    .bindAttribute("a_TexCoord", ChunkShaderBindingPoints.TEX_COORD)
                    .bindAttribute("a_LightCoord", ChunkShaderBindingPoints.LIGHT_COORD)
                    .bindAttribute("d_ModelOffset", ChunkShaderBindingPoints.MODEL_OFFSET)
                    .build((program, name) -> new ChunkProgram(device, program, name, fogMode.getFactory()));
        } finally {
            vertShader.delete();
            fragShader.delete();
        }
    }

    @Override
    public final void createShaders(RenderDevice device) {
        this.programs.put(ChunkFogMode.NONE, this.createShader(device, ChunkFogMode.NONE, this.vertexFormat));
        this.programs.put(ChunkFogMode.LINEAR, this.createShader(device, ChunkFogMode.LINEAR, this.vertexFormat));
        this.programs.put(ChunkFogMode.EXP2, this.createShader(device, ChunkFogMode.EXP2, this.vertexFormat));
    }

    @Override
    public void begin() {
        this.activeProgram = this.programs.get(FogHelper.getFogMode());
        this.activeProgram.bind();
        this.activeProgram.setup(this.vertexType.getModelScale(), this.vertexType.getTextureScale());
    }

    @Override
    public void end() {
        this.activeProgram.unbind();
        this.activeProgram = null;
    }

    @Override
    public void delete() {
        for (ChunkProgram shader : this.programs.values()) {
            shader.delete();
        }
    }

    @Override
    public ChunkVertexType getVertexType() {
        return this.vertexType;
    }
}
