package com.jmod.jrender.client.render.opengl.vao;

import java.util.ArrayList;
import java.util.List;

public class AttributePointersBuilder {
    private final List<VertexAttributePointer> attributePointers;
    private int currentOffset;

    public AttributePointersBuilder(){
        this.attributePointers = new ArrayList<>();
        this.currentOffset = 0;
    }

    private void setStrides(){
        int totalStride = this.currentOffset;
        if (totalStride % 4 != 0) {
            totalStride += (4 - (totalStride % 4));
        }

        for (VertexAttributePointer attributePointer : this.attributePointers) {
            attributePointer.setStride(totalStride);
        }
    }

    public AttributePointersBuilder addAttribute(VertexType vertexType, int size, boolean normalized){
        attributePointers.add(new VertexAttributePointer(
                attributePointers.size(),
                size,
                vertexType.getGlType(),
                normalized,
                0,
                currentOffset
                ));

        currentOffset += size * vertexType.getBytes();

        return this;
    }

    public List<VertexAttributePointer> build(){
        this.setStrides();
        return this.attributePointers;
    }
}
