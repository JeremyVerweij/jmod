package com.jmod.jrender.client.render.opengl.shader;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final AbstractObject2IntMap<String> uniforms;
    private final int programId;

    public ShaderProgram(Shader... shaders){
        this.uniforms = new Object2IntOpenHashMap<>();
        this.programId = glCreateProgram();

        for (Shader shader : shaders) {
            shader.attach(this.programId);
        }

        glLinkProgram(this.programId);

        int success = glGetProgrami(this.programId, GL_LINK_STATUS);
        if(success == GL_FALSE){
            throw new RuntimeException(glGetProgramInfoLog(this.programId, 512));
        }

        bind();

        for (Shader shader : shaders) {
            shader.delete();
        }

        unbind();
    }

    public ShaderProgram registerUniform(String uniformName){
        this.uniforms.put(uniformName, glGetUniformLocation(this.programId, uniformName));

        return this;
    }

    public int getUniform(String uniformName){
        return this.uniforms.getInt(uniformName);
    }

    public void uploadUniform(String uniformName, Matrix4f value){
//        try(MemoryStack stack = MemoryStack.stackPush()){
//            glUniformMatrix4(getUniform(uniformName), false, value.get(stack.mallocFloat(16)));
//        }
    }

    public void uploadUniform(String uniformName, int value){
        glUniform1i(getUniform(uniformName), value);
    }

    public void uploadUniform(String uniformName, float value1, float value2){
        glUniform2f(getUniform(uniformName), value1, value2);
    }

    public void bind(){
        glUseProgram(this.programId);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void delete(){
        glDeleteProgram(this.programId);
    }
}
