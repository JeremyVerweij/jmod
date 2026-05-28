package com.jmod.jrender.client.render.world.chunk;

import com.jmod.jrender.client.render.opengl.ChunkBufferBuilder;
import com.jmod.jrender.client.render.opengl.vao.AttributePointersBuilder;
import com.jmod.jrender.client.render.opengl.vao.VertexArrayObject;
import com.jmod.jrender.client.render.world.JRenderGlobal;
import com.jmod.jrender.client.render.world.chunk.data.ChunkDataNonEmpty;
import net.minecraft.util.math.BlockPos;

import static org.lwjglx.opengl.GL11.GL_QUADS;

public class CompiledRenderChunk {
    private final JRenderGlobal renderGlobal;
    private final VertexArrayObject vao;
    private final BlockPos.MutableBlockPos position;

    private ChunkDataNonEmpty chunkData;

    public CompiledRenderChunk(JRenderGlobal renderGlobal, AttributePointersBuilder builder) {
        this.renderGlobal = renderGlobal;
        this.vao = new VertexArrayObject(builder.build());
        this.position = new BlockPos.MutableBlockPos(0, 0, 0);
    }

    public void setPos(int x, int z){
        this.position.setPos(x, 0, z);
    }

    public void draw(){
        this.vao.draw(GL_QUADS);
    }

    public void loadData(){
        this.chunkData = new ChunkDataNonEmpty(
                this.renderGlobal.getWorld().getChunk(this.position.getX(), this.position.getZ()));
    }

    public void build(ChunkBufferBuilder builder){
        float xStart, yStart, zStart;
        float xEnd, yEnd, zEnd;

        for (ChunkDataNonEmpty.ChunkDataEntry blockData : this.chunkData.getData()) {
            xStart = blockData.x();
            yStart = blockData.y();
            zStart = blockData.z();
            xEnd = xStart + 1;
            yEnd = yStart + 1;
            zEnd = zStart + 1;

            builder.putPos(xStart,  yEnd,   zEnd    ).putColor(0xFFFFFFFF).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yEnd,   zEnd    ).putColor(0xFFFFFFFF).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yEnd,   zStart  ).putColor(0xFFFFFFFF).putUV(0, 0).endVertex()
                    .putPos(xStart, yEnd,   zStart  ).putColor(0xFFFFFFFF).putUV(0, 0).endVertex()
                    .putPos(xStart, yStart, zStart  ).putColor(0xFFFF0000).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yStart, zStart  ).putColor(0xFFFF0000).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yStart, zEnd    ).putColor(0xFFFF0000).putUV(0, 0).endVertex()
                    .putPos(xStart, yStart, zEnd    ).putColor(0xFFFF0000).putUV(0, 0).endVertex()
                    .putPos(xStart, yStart, zEnd    ).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yStart, zEnd    ).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yEnd,   zEnd    ).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                    .putPos(xStart, yEnd,   zEnd    ).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                    .putPos(xStart, yEnd,   zStart  ).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yEnd,   zStart  ).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yStart, zStart  ).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                    .putPos(xStart, yStart, zStart  ).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                    .putPos(xStart, yStart, zStart  ).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                    .putPos(xStart, yStart, zEnd    ).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                    .putPos(xStart, yEnd,   zEnd    ).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                    .putPos(xStart, yEnd,   zStart  ).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yEnd,   zStart  ).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yEnd,   zEnd    ).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yStart, zEnd    ).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                    .putPos(xEnd,   yStart, zStart  ).putColor(0xFF00FF00).putUV(0, 0).endVertex();
        }

        this.vao.upload(builder);
    }

    public void delete(){
        this.vao.delete();
    }

    public BlockPos.MutableBlockPos getPosition() {
        return position;
    }
}
