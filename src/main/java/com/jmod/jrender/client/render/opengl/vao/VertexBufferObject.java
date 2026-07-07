package com.jmod.jrender.client.render.opengl.vao;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static com.jmod.jrender.client.render.opengl.vao.VertexArrayObject.MINECRAFT_VBO;
import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject {
    private final int id;

    public VertexBufferObject(){
        this.id = glGenBuffers();
    }

    public void bind(){
        glBindBuffer(GL_ARRAY_BUFFER, this.id);
    }

    public void unbind(){
        glBindBuffer(GL_ARRAY_BUFFER, MINECRAFT_VBO);
    }

    public void putData(ByteBuffer buffer){
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public void putData(FloatBuffer buffer){
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public void delete() {
        glDeleteBuffers(this.id);
    }
}
