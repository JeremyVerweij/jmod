package com.jmod.jrender.client.render.opengl.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int id;

    public Shader(String shaderSrc, ShaderType shaderType){
        this.id = glCreateShader(shaderType.getGlShaderType());
        glShaderSource(this.id, shaderSrc);
        glCompileShader(this.id);

        int success = glGetShaderi(this.id, GL_COMPILE_STATUS);

        if(success == GL_FALSE){
            throw new RuntimeException(glGetShaderInfoLog(this.id, 512));
        }
    }

    public void attach(int programId){
        glAttachShader(programId, this.id);
    }

    public void delete(){
        glDeleteShader(this.id);
    }
}
