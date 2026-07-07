package com.jmod.jrender.client.render.opengl.shader;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public enum ShaderType {
    VERTEX(GL_VERTEX_SHADER), FRAGMENT(GL_FRAGMENT_SHADER);

    private final int glShaderType;

    ShaderType(int glShaderType) {
        this.glShaderType = glShaderType;
    }

    public int getGlShaderType() {
        return glShaderType;
    }
}
