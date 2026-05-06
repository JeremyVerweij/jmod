package com.jmod.jrender.client.render.chunk.cull;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import com.jmod.jrender.client.util.math.FrustumExtended;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.util.math.Vec3d;

public interface ChunkCuller {
    IntArrayList computeVisible(Vec3d cameraPos, FrustumExtended frustum, int frame, boolean spectator);

    void onSectionStateChanged(int x, int y, int z, SetVisibility occlusionData);
    void onSectionLoaded(int x, int y, int z, int id);
    void onSectionUnloaded(int x, int y, int z);

    boolean isSectionVisible(int x, int y, int z);
}
