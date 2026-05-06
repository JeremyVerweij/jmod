package com.jmod.core.client.model;

import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.vecmath.Vector4f;
import java.util.Arrays;

public class UVTransformer extends net.minecraftforge.client.model.pipeline.VertexTransformer {
    private final UVMapper mapper;
    private final float[][][] vertexData;
    private int index = 0;

    public UVTransformer(IVertexConsumer consumer, UVMapper mapper) {
        super(consumer);
        this.mapper = mapper;
        this.vertexData = new float[4][consumer.getVertexFormat().getElementCount()][4];
    }

    @Override
    public void put(int element, float... data) {
        if (this.getVertexFormat().getElement(element).getUsage() == VertexFormatElement.EnumUsage.UV) {
            this.mapper.putUV(this.index, data[0], data[1]);
        }

        this.vertexData[this.index][element] = Arrays.copyOf(data, data.length);

        if (element == this.parent.getVertexFormat().getElementCount() - 1){
            this.index++;
        }
    }

    public void apply(){
        for (int vertex = 0; vertex < this.vertexData.length; vertex++) {
            for (int element = 0; element < this.vertexData[vertex].length; element++) {
                float[] data = this.vertexData[vertex][element];

                if (this.parent.getVertexFormat().getElement(element).getUsage() == VertexFormatElement.EnumUsage.UV){
                    super.put(element, this.mapper.getU(vertex), this.mapper.getV(vertex));
                }else{
                    super.put(element, data);
                }
            }
        }
    }

    public static class UVMapper{
        private final int[] mapper;
        private final float[] uvs;

        public UVMapper(int v1, int v2, int v3, int v4){
            this.mapper = new int[]{v1, v2, v3, v4};
            this.uvs = new float[8];
        }

        public void putUV(int vertex, float u, float v){
            this.uvs[this.mapper[vertex] * 2] = u;
            this.uvs[(this.mapper[vertex] * 2) + 1] = v;
        }

        public float getU(int vertex){
            return this.uvs[vertex * 2];
        }

        public float getV(int vertex){
            return this.uvs[vertex * 2 + 1];
        }

        public static UVMapper create0DegreeRotation(){
            return new UVMapper(0, 1, 2, 3);
        }

        public static UVMapper create90DegreeRotation(){
            return new UVMapper(3, 0, 1, 2);
        }

        public static UVMapper create180DegreeRotation(){
            return new UVMapper(2, 3, 0, 1);
        }

        public static UVMapper create270DegreeRotation(){
            return new UVMapper(1, 2, 3, 0);
        }
    }
}
