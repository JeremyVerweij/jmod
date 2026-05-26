package com.jmod.jrender.client.render.opengl.vao;

import com.jmod.jrender.client.render.opengl.ChunkBufferBuilder;

import java.util.List;

import static org.lwjglx.opengl.GL11.*;
import static org.lwjglx.opengl.GL15.GL_ARRAY_BUFFER_BINDING;
import static org.lwjglx.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjglx.opengl.GL20.glVertexAttribPointer;
import static org.lwjglx.opengl.GL30.*;

public class VertexArrayObject {
    public static int MINECRAFT_VAO;
    public static int MINECRAFT_VBO;
    private final int id;
    private final VertexBufferObject vbo;
    private int vertices = 0;

    public static void loadMinecraftDefaults(){
        MINECRAFT_VAO = glGetInteger(GL_VERTEX_ARRAY_BINDING);
        MINECRAFT_VBO = glGetInteger(GL_ARRAY_BUFFER_BINDING);
    }

    public VertexArrayObject(List<VertexAttributePointer> vertexAttributePointers) {
        this.id = glGenVertexArrays();
        this.vbo = new VertexBufferObject();

        this.bind();
        this.vbo.bind();

        for (VertexAttributePointer vertexAttributePointer : vertexAttributePointers) {
            glVertexAttribPointer(vertexAttributePointer.index(), vertexAttributePointer.size(),
                    vertexAttributePointer.glType(), vertexAttributePointer.normalized(),
                    vertexAttributePointer.stride(), vertexAttributePointer.offset());

            glEnableVertexAttribArray(vertexAttributePointer.index());
        }

        this.unbind();
        this.vbo.unbind();
    }

    public void bind(){
        glBindVertexArray(this.id);
    }

    public void unbind(){
        glBindVertexArray(MINECRAFT_VAO);
    }

    public void upload(ChunkBufferBuilder builder){
        builder.flip();
        this.vertices = builder.getVertices();

        this.bind();
        this.vbo.bind();
        this.vbo.putData(builder.getBuffer());
        this.unbind();
        this.vbo.unbind();
    }

    public void draw(int type){
        this.bind();
        glDrawArrays(type, 0, this.vertices);
        this.unbind();
    }

    public void delete(){
        this.vbo.delete();
        glDeleteVertexArrays(this.id);
    }
}
