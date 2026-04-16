package com.jmod.core.common.utils.random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec2f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RotationUtils {
    public enum EnumSide2D {
        UP, BOTTOM, LEFT, RIGHT
    }

    public static @Nullable EnumFacing rotateSide(@Nonnull EnumFacing side, @Nonnull EnumSide2D rotation){
        switch (rotation){
            case LEFT:
                return (side.getAxis().isHorizontal()) ? (side == EnumFacing.WEST || side == EnumFacing.SOUTH) ? side.rotateY() :
                        side.rotateY().getOpposite() : EnumFacing.WEST;
            case RIGHT:
                return (side.getAxis().isHorizontal()) ? (side == EnumFacing.WEST || side == EnumFacing.SOUTH) ? side.rotateY().getOpposite() :
                        side.rotateY() : EnumFacing.EAST;
            case UP:
                return (side.getAxis().isHorizontal()) ? EnumFacing.UP : EnumFacing.SOUTH;
            case BOTTOM:
                return (side.getAxis().isHorizontal()) ? EnumFacing.DOWN : EnumFacing.NORTH;
        }

        return null;
    }

    public static @Nullable EnumFacing rotateSideNoCorrection(@Nonnull EnumFacing side, @Nonnull EnumSide2D rotation){
        switch (rotation){
            case LEFT:
                return (side.getAxis().isHorizontal()) ? side.rotateY() : EnumFacing.WEST;
            case RIGHT:
                return (side.getAxis().isHorizontal()) ? side.rotateY().getOpposite() : EnumFacing.EAST;
            case UP:
                return (side.getAxis().isHorizontal()) ? EnumFacing.UP : EnumFacing.SOUTH;
            case BOTTOM:
                return (side.getAxis().isHorizontal()) ? EnumFacing.DOWN : EnumFacing.NORTH;
        }

        return null;
    }

    public static Vec2f getUV(EnumFacing side, float hitX, float hitY, float hitZ) {
        float u = 0, v = 0;
        switch (side) {
            case NORTH: u = 1 - hitX; v = hitY; break;
            case SOUTH: u = hitX;     v = hitY; break;
            case WEST:  u = hitZ;     v = hitY; break;
            case EAST:  u = 1 - hitZ; v = hitY; break;
            case UP:    u = hitX;     v = hitZ; break;
            case DOWN:  u = hitX;     v = 1 - hitZ; break;
        }
        return new Vec2f(u, v);
    }

    public static boolean isInBoundingBox2D(Vec2f UV, double minX, double minY, double maxX, double maxY){
        return UV.x > minX && UV.x < maxX && UV.y > minY && UV.y < maxY;
    }
}
