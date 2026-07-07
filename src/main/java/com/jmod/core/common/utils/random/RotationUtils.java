package com.jmod.core.common.utils.random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class RotationUtils {
    public enum EnumSide2D {
        UP, BOTTOM, LEFT, RIGHT
    }

    public static @NonNull EnumFacing rotateSide(@NotNull EnumFacing side, @NotNull EnumSide2D rotation){
        return switch (rotation) {
            case LEFT ->
                    (side.getAxis().isHorizontal()) ? (side == EnumFacing.WEST || side == EnumFacing.SOUTH) ? side.rotateY() :
                            side.rotateY().getOpposite() : EnumFacing.WEST;
            case RIGHT ->
                    (side.getAxis().isHorizontal()) ? (side == EnumFacing.WEST || side == EnumFacing.SOUTH) ? side.rotateY().getOpposite() :
                            side.rotateY() : EnumFacing.EAST;
            case UP -> (side.getAxis().isHorizontal()) ? EnumFacing.UP : EnumFacing.SOUTH;
            case BOTTOM -> (side.getAxis().isHorizontal()) ? EnumFacing.DOWN : EnumFacing.NORTH;
        };

    }

    public static @NonNull EnumFacing rotateSideNoCorrection(@NotNull EnumFacing side, @NotNull EnumSide2D rotation){
        return switch (rotation) {
            case LEFT -> (side.getAxis().isHorizontal()) ? side.rotateY() : EnumFacing.WEST;
            case RIGHT -> (side.getAxis().isHorizontal()) ? side.rotateY().getOpposite() : EnumFacing.EAST;
            case UP -> (side.getAxis().isHorizontal()) ? EnumFacing.UP : EnumFacing.SOUTH;
            case BOTTOM -> (side.getAxis().isHorizontal()) ? EnumFacing.DOWN : EnumFacing.NORTH;
        };

    }

    public static Vec2f getUV(EnumFacing side, float hitX, float hitY, float hitZ) {
        float u, v;
        v = switch (side) {
            case NORTH -> {
                u = 1 - hitX;
                yield hitY;
            }
            case SOUTH -> {
                u = hitX;
                yield hitY;
            }
            case WEST -> {
                u = hitZ;
                yield hitY;
            }
            case EAST -> {
                u = 1 - hitZ;
                yield hitY;
            }
            case UP, DOWN -> {
                u = hitX;
                yield hitZ;
            }
        };
        return new Vec2f(u, v);
    }

    public static boolean isInBoundingBox2D(Vec2f UV, double minX, double minY, double maxX, double maxY){
        return UV.x > minX && UV.x < maxX && UV.y > minY && UV.y < maxY;
    }
}
