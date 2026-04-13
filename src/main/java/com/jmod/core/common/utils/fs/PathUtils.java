package com.jmod.core.common.utils.fs;

import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {
    public static @Nonnull Path getSavePath(){
        return Paths.get(DimensionManager.getCurrentSaveRootDirectory().getPath());
    }

    public static @Nonnull Path getJModWorldPath(){
        return Paths.get(getSavePath().toString(), "jmod");
    }

    public static @Nonnull Path getJModIDPath(int dimension){
        return Paths.get(getSavePath().toString(), "jmod", "DIM" + dimension, "ids");
    }

    public static @Nonnull Path getRegionFile(int chunkX, int chunkZ, int dimension){
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;

        return Paths.get(getJModIDPath(dimension).toString(), "r." + regionX + "." + regionZ + ".jreg");
    }
}
