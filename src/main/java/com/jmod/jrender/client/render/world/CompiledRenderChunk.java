package com.jmod.jrender.client.render.world;

import com.jmod.jrender.client.render.opengl.ChunkBufferBuilder;
import com.jmod.jrender.client.render.opengl.vao.AttributePointersBuilder;
import com.jmod.jrender.client.render.opengl.vao.VertexArrayObject;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import static org.lwjglx.opengl.GL11.GL_QUADS;

public class CompiledRenderChunk {
    private final JRenderGlobal renderGlobal;
    private final VertexArrayObject vao;
    private final BlockPos.MutableBlockPos positionStart;
    private final BlockPos.MutableBlockPos positionEnd;

    public CompiledRenderChunk(JRenderGlobal renderGlobal, AttributePointersBuilder builder) {
        this.renderGlobal = renderGlobal;
        this.vao = new VertexArrayObject(builder.build());
        this.positionStart = new BlockPos.MutableBlockPos(0, 0, 0);
        this.positionEnd = new BlockPos.MutableBlockPos(0, 255, 0);
    }

    public void setPos(int x, int z){
        this.positionStart.setPos(x << 4, 0, z << 4);
        this.positionEnd.setPos((x << 4) + 15, 255, (z << 4) + 15);
    }

    public void draw(){
        this.vao.draw(GL_QUADS);
    }

    public void build(ChunkBufferBuilder builder){
        float xStart, yStart, zStart;
        float xEnd, yEnd, zEnd;

        WorldClient world = this.renderGlobal.getWorld();

        for (BlockPos pos : BlockPos.getAllInBox(this.positionStart.toImmutable(), this.positionEnd.toImmutable())) {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (block == Blocks.AIR) continue;

            xStart = pos.getX();
            yStart = pos.getY();
            zStart = pos.getZ();
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
        return positionStart;
    }
}
