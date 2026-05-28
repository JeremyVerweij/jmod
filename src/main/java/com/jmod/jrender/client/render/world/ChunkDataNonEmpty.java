package com.jmod.jrender.client.render.world;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class ChunkDataNonEmpty {

    public static class ChunkNonEmptyEntry{
        private final IBakedModel model;
        private final int x;
        private final int y;
        private final byte enabledSides;

        public ChunkNonEmptyEntry(IBakedModel model, int x, int y, byte enabledSides){
            this.model = model;
            this.x = x;
            this.y = y;
            this.enabledSides = enabledSides;
        }

        public List<BakedQuad> getBakedQuadsForSide(EnumFacing side){
            return this.model.getQuads(null, side, 0);
        }

        public List<BakedQuad> getAllBakedQuads(){
            List<BakedQuad> allQuads = new ArrayList<>(getBakedQuadsForSide(null));

            for (EnumFacing facing : EnumFacing.values()) {
                boolean enabled = ((1 << facing.getIndex()) & this.enabledSides) > 0;
                if (enabled){
                    getBakedQuadsForSide(facing);
                }
            }

            return allQuads;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
