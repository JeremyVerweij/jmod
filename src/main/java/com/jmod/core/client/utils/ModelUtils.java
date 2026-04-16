package com.jmod.core.client.utils;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;

public class ModelUtils {
    private static final FaceBakery BAKERY = new FaceBakery();

    public static @Nonnull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex, Vector3f from, Vector3f to, float[] uvCoords) {
        BlockFaceUV uv = new BlockFaceUV(uvCoords, 0);

        BlockPartFace face = new BlockPartFace(side, tintIndex, "", uv);

        return BAKERY.makeBakedQuad(
                from, to, face, sprite, side,
                ModelRotation.X0_Y0, // No rotation
                (BlockPartRotation) null,
                true, // scale
                true  // uvLocked
        );
    }

    public static @Nonnull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex, Vector3f from, Vector3f to) {
        float[] uvCoords = new float[]{0, 0, 16, 16};
        return createQuad(side, sprite, tintIndex, from, to, uvCoords);
    }

    public static @Nonnull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex, float[] uvCoords) {
        // Define the cube corners (0 to 16 is a full block)
        Vector3f from = new Vector3f(0, 0, 0);
        Vector3f to = new Vector3f(16, 16, 16);
        return createQuad(side, sprite, tintIndex, from, to, uvCoords);
    }

    public static @Nonnull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex) {
        // Define the cube corners (0 to 16 is a full block)
        Vector3f from = new Vector3f(0, 0, 0);
        Vector3f to = new Vector3f(16, 16, 16);

        return createQuad(side, sprite, tintIndex, from, to);
    }
}
