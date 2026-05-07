package com.jmod.jrender.client.util.math;

import com.jmod.jrender.client.util.Norm3b;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MatrixUtil {
    public static int computeNormal(Matrix3f matrix, EnumFacing dir) {
        Vec3i faceNorm = dir.getDirectionVec();

        float x = faceNorm.getX();
        float y = faceNorm.getY();
        float z = faceNorm.getZ();

        float x2 = matrix.m00 * x + matrix.m01 * y + matrix.m02 * z;
        float y2 = matrix.m10 * x + matrix.m11 * y + matrix.m12 * z;
        float z2 = matrix.m20 * x + matrix.m21 * y + matrix.m22 * z;

        return Norm3b.pack(x2, y2, z2);
    }

    public static float transformVecX(Matrix3f matrix, float x, float y, float z) {
        return matrix.m00 * x + matrix.m01 * y + matrix.m02 * z;
    }

    public static float transformVecY(Matrix3f matrix, float x, float y, float z) {
        return matrix.m10 * x + matrix.m11 * y + matrix.m12 * z;
    }

    public static float transformVecZ(Matrix3f matrix, float x, float y, float z) {
        return matrix.m20 * x + matrix.m21 * y + matrix.m22 * z;
    }

    public static float transformVecX(Matrix4f matrix, float x, float y, float z) {
        return (matrix.m00() * x) + (matrix.m01() * y) + (matrix.m02() * z) + (matrix.m03());
    }

    public static float transformVecY(Matrix4f matrix, float x, float y, float z) {
        return (matrix.m10() * x) + (matrix.m11() * y) + (matrix.m12() * z) + (matrix.m13());
    }

    public static float transformVecZ(Matrix4f matrix, float x, float y, float z) {
        return (matrix.m20() * x) + (matrix.m21() * y) + (matrix.m22() * z) + (matrix.m23());
    }

    public static int transformPackedNormal(int norm, Matrix3f matrix) {
        float normX1 = Norm3b.unpackX(norm);
        float normY1 = Norm3b.unpackY(norm);
        float normZ1 = Norm3b.unpackZ(norm);

        float normX2 = transformVecX(matrix, normX1, normY1, normZ1);
        float normY2 = transformVecY(matrix, normX1, normY1, normZ1);
        float normZ2 = transformVecZ(matrix, normX1, normY1, normZ1);

        return Norm3b.pack(normX2, normY2, normZ2);
    }
}
