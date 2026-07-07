package com.jmod.core.common.utils.fs;

import net.minecraftforge.common.DimensionManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {
    public static @NotNull Path getSavePath(){
        return Paths.get(DimensionManager.getCurrentSaveRootDirectory().getPath());
    }

    public static @NotNull Path getJModWorldPath(){
        return Paths.get(getSavePath().toString(), "jmod");
    }

    public static @NotNull Path getJModIDPath(int dimension){
        return Paths.get(getSavePath().toString(), "jmod", "DIM" + dimension, "ids");
    }

    public static @NotNull Path getRegionFile(int chunkX, int chunkZ, int dimension){
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;

        return Paths.get(getJModIDPath(dimension).toString(), "r." + regionX + "." + regionZ + ".jreg");
    }
}
