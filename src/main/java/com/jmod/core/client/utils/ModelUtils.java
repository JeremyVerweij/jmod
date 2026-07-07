package com.jmod.core.client.utils;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.ITransformation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Vector3f;

public class ModelUtils {
    private static final FaceBakery BAKERY = new FaceBakery();

    public static float[] resizeUVForLargerTexture(float texSize, float[] uvs){
        float div = texSize / 16f;

        return new float[]{uvs[0] / div, uvs[1] / div, uvs[2] / div, uvs[3] / div};
    }

    public static @NotNull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex, Vector3f from, Vector3f to, float[] uvCoords, ITransformation rotation) {
        BlockFaceUV uv = new BlockFaceUV(uvCoords, 0);

        BlockPartFace face = new BlockPartFace(side, tintIndex, "", uv);

        return BAKERY.makeBakedQuad(
                from, to, face, sprite, side,
                rotation, // No rotation
                null,
                false, // uvLocked
                true  // shade
        );
    }

    public static @NotNull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex, Vector3f from, Vector3f to, float[] uvCoords) {
        return createQuad(side, sprite, tintIndex, from, to, uvCoords, ModelRotation.X0_Y0);
    }

    public static @NotNull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex, Vector3f from, Vector3f to, ITransformation rotation) {
        float[] uvCoords = new float[]{0, 0, 16, 16};
        return createQuad(side, sprite, tintIndex, from, to, uvCoords, rotation);
    }

    public static @NotNull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex, Vector3f from, Vector3f to) {
        float[] uvCoords = new float[]{0, 0, 16, 16};
        return createQuad(side, sprite, tintIndex, from, to, uvCoords);
    }

    public static @NotNull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex, float[] uvCoords) {
        // Define the cube corners (0 to 16 is a full block)
        Vector3f from = new Vector3f(0, 0, 0);
        Vector3f to = new Vector3f(16, 16, 16);
        return createQuad(side, sprite, tintIndex, from, to, uvCoords);
    }

    public static @NotNull BakedQuad createQuad(EnumFacing side, TextureAtlasSprite sprite, int tintIndex) {
        // Define the cube corners (0 to 16 is a full block)
        Vector3f from = new Vector3f(0, 0, 0);
        Vector3f to = new Vector3f(16, 16, 16);
        return createQuad(side, sprite, tintIndex, from, to);
    }
}
