package com.jmod.jrender.client.render.world.chunk;

import com.jmod.jrender.client.render.opengl.ChunkBufferBuilder;
import com.jmod.jrender.client.render.opengl.vao.AttributePointersBuilder;
import com.jmod.jrender.client.render.opengl.vao.VertexArrayObject;
import com.jmod.jrender.client.render.world.JRenderGlobal;
import com.jmod.jrender.client.render.world.chunk.data.RawChunkRenderData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import static org.lwjglx.opengl.GL11.GL_QUADS;

public class JRenderChunk implements Runnable{
    private final JRenderGlobal renderGlobal;
    private final VertexArrayObject vao;
    private final BlockPos.MutableBlockPos position;
    private final RawChunkRenderData chunkData;
    private final ChunkBufferBuilder builder;
    private boolean isBuilt = false;
    private boolean stopped = false;

    public JRenderChunk(JRenderGlobal renderGlobal, AttributePointersBuilder builder) {
        this.renderGlobal = renderGlobal;
        this.vao = new VertexArrayObject(builder.build());
        this.position = new BlockPos.MutableBlockPos(Integer.MIN_VALUE, 0, Integer.MIN_VALUE);
        this.chunkData = new RawChunkRenderData();
        this.builder = new ChunkBufferBuilder();
    }

    @Override
    public void run() {
        this.builder.reset();
        this.build(this.builder);
    }

    public void setPos(int x, int z){
        this.position.setPos(x, 0, z);
    }

    public void draw(){
        this.vao.draw(GL_QUADS);
    }

    public void loadData(){
        this.chunkData.loadData(this.renderGlobal.getWorld().getChunk(this.position.getX(), this.position.getZ()));
    }

    public void delete(){
        this.vao.delete();
    }

    public void upload(){
        this.vao.upload(this.builder);
    }

    public void stop(){
        this.stopped = true;
    }

    public BlockPos.MutableBlockPos getPosition() {
        return this.position;
    }

    public boolean isBuilt() {
        return this.isBuilt;
    }

    public void build(ChunkBufferBuilder builder){
        this.isBuilt = false;
        this.stopped = false;

        float xStart, yStart, zStart;
        float xEnd, yEnd, zEnd;
        int x, y, z;

        IBlockState[] data = this.chunkData.getData();

        for (int index = 0; index < data.length; index++) {
            if (this.stopped) return;

            IBlockState state = data[index];

            //skip if no block
            if (state == null) continue;
            if (state.getBlock() == Blocks.AIR) continue;

            //get position from index
            x = this.chunkData.getX(index);
            y = this.chunkData.getY(index);
            z = this.chunkData.getZ(index);

            //check if surrounded by solid blocks
            if (this.isInvisibleByNeighbours(x, y, z)) continue;

            //get world position
            xStart = x + (this.position.getX() << 4);
            yStart = y;
            zStart = z + (this.position.getZ() << 4);
            xEnd = xStart + 1;
            yEnd = yStart + 1;
            zEnd = zStart + 1;

            byte shownFaces = getShownFaces(x, y, z);

            //place quads
            if (checkShowFaces(shownFaces, EnumFacing.UP))
                builder .putPos(xStart, yEnd,   zEnd    ).putColor(0xFFFFFFFF).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yEnd,   zEnd    ).putColor(0xFFFFFFFF).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yEnd,   zStart  ).putColor(0xFFFFFFFF).putUV(0, 0).endVertex()
                        .putPos(xStart, yEnd,   zStart  ).putColor(0xFFFFFFFF).putUV(0, 0).endVertex();

            if (checkShowFaces(shownFaces, EnumFacing.DOWN))
                builder .putPos(xStart, yStart, zStart  ).putColor(0xFFFF0000).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yStart, zStart  ).putColor(0xFFFF0000).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yStart, zEnd    ).putColor(0xFFFF0000).putUV(0, 0).endVertex()
                        .putPos(xStart, yStart, zEnd    ).putColor(0xFFFF0000).putUV(0, 0).endVertex();

            if (checkShowFaces(shownFaces, EnumFacing.SOUTH))
                builder .putPos(xStart, yStart, zEnd    ).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yStart, zEnd    ).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yEnd,   zEnd    ).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                        .putPos(xStart, yEnd,   zEnd    ).putColor(0xFFFF00FF).putUV(0, 0).endVertex();

            if (checkShowFaces(shownFaces, EnumFacing.NORTH))
                builder .putPos(xStart, yEnd,   zStart  ).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yEnd,   zStart  ).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yStart, zStart  ).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                        .putPos(xStart, yStart, zStart  ).putColor(0xFFFFFF00).putUV(0, 0).endVertex();

            if (checkShowFaces(shownFaces, EnumFacing.EAST))
                builder .putPos(xEnd,   yEnd,   zStart  ).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yEnd,   zEnd    ).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yStart, zEnd    ).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                        .putPos(xEnd,   yStart, zStart  ).putColor(0xFF00FF00).putUV(0, 0).endVertex();

            if (checkShowFaces(shownFaces, EnumFacing.WEST))
                builder .putPos(xStart, yStart, zStart  ).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                        .putPos(xStart, yStart, zEnd    ).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                        .putPos(xStart, yEnd,   zEnd    ).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                        .putPos(xStart, yEnd,   zStart  ).putColor(0xFF0000FF).putUV(0, 0).endVertex();
        }

        this.isBuilt = true;
    }

    private boolean checkShowFaces(byte shownFaces, EnumFacing side){
        return ((1 << side.getIndex()) & shownFaces) > 0;
    }

    private byte getShownFaces(int x, int y, int z){
        byte shownFaces = 0;
        for (EnumFacing side : EnumFacing.values()) {
            Vec3i offset = side.getDirectionVec();
            IBlockState neighbourState = this.chunkData.getBlockData(x + offset.getX(), y + offset.getY(), z + offset.getZ());

            if (neighbourState == null || !neighbourState.isFullCube()) shownFaces |= (byte) (1 << side.getIndex());
        }

        return shownFaces;
    }

    private boolean isInvisibleByNeighbours(int x, int y, int z){
        for (int neighbourOffset = -1; neighbourOffset <= 1; neighbourOffset++) {
            IBlockState neighbour;

            if ((neighbour = this.chunkData.getBlockData(x + neighbourOffset, y, z)) == null || !neighbour.isFullCube()){
                return false;
            }
            if ((neighbour = this.chunkData.getBlockData(x, y + neighbourOffset, z)) == null || !neighbour.isFullCube()){
                return false;
            }
            if ((neighbour = this.chunkData.getBlockData(x, y, z + neighbourOffset)) == null || !neighbour.isFullCube()){
                return false;
            }
        }
        return true;
    }
}
