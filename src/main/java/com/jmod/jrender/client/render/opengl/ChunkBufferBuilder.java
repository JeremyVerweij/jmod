package com.jmod.jrender.client.render.opengl;

import org.lwjgl.BufferUtils;

import java.nio.*;

import static com.jmod.jrender.client.util.ColorARGB.toColorRGBA;
import static com.jmod.jrender.client.util.ColorRGBA.invertBytes;

public class ChunkBufferBuilder {
    private ByteBuffer buffer;
    private int vertices = 0;

    public ChunkBufferBuilder(){
        this.buffer = BufferUtils.createByteBuffer(32_768);
    }

    private void ensureCapacity(int additionalBytes) {
        if (this.buffer.position() + additionalBytes > this.buffer.capacity()) {
            int newCapacity = this.buffer.capacity() * 2;
            ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);

            this.buffer.flip();
            newBuffer.put(this.buffer);

            this.buffer = newBuffer;
        }
    }

    public ChunkBufferBuilder putFloat(float value) {
        ensureCapacity(Float.BYTES);
        int currentBytePos = this.buffer.position();
        this.buffer.putFloat(currentBytePos, value);
        this.buffer.position(currentBytePos + Float.BYTES);
        return this;
    }

    public ChunkBufferBuilder putByte(byte value) {
        ensureCapacity(Byte.BYTES);
        int currentBytePos = this.buffer.position();
        this.buffer.put(currentBytePos, value);
        this.buffer.position(currentBytePos + Byte.BYTES);
        return this;
    }

    public ChunkBufferBuilder putShort(short value) {
        ensureCapacity(Short.BYTES);
        int currentBytePos = this.buffer.position();
        this.buffer.putShort(currentBytePos, value);
        this.buffer.position(currentBytePos + Short.BYTES);
        return this;
    }

    public ChunkBufferBuilder putInt(int value) {
        ensureCapacity(Integer.BYTES);
        int currentBytePos = this.buffer.position();
        this.buffer.putInt(currentBytePos, value);
        this.buffer.position(currentBytePos + Integer.BYTES);
        return this;
    }

    public ChunkBufferBuilder padding(byte padding){
        int currentBytePos = this.buffer.position();
        this.buffer.position(currentBytePos + padding);
        return this;
    }

    public ChunkBufferBuilder endVertex(){
        this.vertices += 1;
        return this;
    }

    public ChunkBufferBuilder endVertex(int padding){
        this.vertices += 1;
        return padding((byte) padding);
    }

    public void flip(){
        this.buffer.flip();
    }

    public void reset(){
        this.buffer.clear();
        this.vertices = 0;
    }

    public ByteBuffer getBuffer(){
        return this.buffer;
    }

    public int getVertices() {
        return vertices;
    }

    public ChunkBufferBuilder putPos(float x, float y, float z){
        putFloat(x);
        putFloat(y);
        return putFloat(z);
    }

    public ChunkBufferBuilder putColor(int color){
        color = toColorRGBA(color);

        if(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN)
            return putInt(color);
        else return putInt(invertBytes(color));
    }

    public ChunkBufferBuilder putUV(short u, short v){
        putShort(u);
        return putShort(v);
    }

    public ChunkBufferBuilder putUV(int u, int v){
        return putUV((short) u, (short) v);
    }

    public ChunkBufferBuilder putUV(float u, float v){
        return putUV((short) u, (short) v);
    }
}
