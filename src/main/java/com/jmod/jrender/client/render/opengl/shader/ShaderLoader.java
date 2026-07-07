package com.jmod.jrender.client.render.opengl.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ShaderLoader {
    public static String loadShaderSrc(ResourceLocation location){
        try {
            IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
            IResource resource = manager.getResource(location);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            System.err.println("Failed to load shader source at: " + location);
            e.printStackTrace();
            return "";
        }
    }

    public static Shader loadShader(ResourceLocation location, ShaderType shaderType){
        return new Shader(loadShaderSrc(location), shaderType);
    }

    public static ShaderProgram loadShaderProgram(ResourceLocation vertexShaderLocation, ResourceLocation fragmentShaderLocation){
        return new ShaderProgram(loadShader(vertexShaderLocation, ShaderType.VERTEX), loadShader(fragmentShaderLocation, ShaderType.FRAGMENT));
    }
}
