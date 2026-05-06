package com.jmod.jrender.client.gl.shader;

public class ShaderBindingPoint {
    private final int genericAttributeIndex;

    public ShaderBindingPoint(int genericAttributeIndex) {
        this.genericAttributeIndex = genericAttributeIndex;
    }

    public int getGenericAttributeIndex() {
        return genericAttributeIndex;
    }
}
