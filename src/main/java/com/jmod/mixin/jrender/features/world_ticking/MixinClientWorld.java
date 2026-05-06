package com.jmod.mixin.jrender.features.world_ticking;

import com.jmod.jrender.client.util.rand.XoRoShiRoRandom;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(WorldClient.class)
public abstract class MixinClientWorld extends World {

    protected MixinClientWorld(ISaveHandler saveHandler, WorldInfo worldInfo, WorldProvider worldProvider, Profiler profiler, boolean client) {
        super(saveHandler, worldInfo, worldProvider, profiler, client);
    }

    @Redirect(method = "doVoidFogParticles", at = @At(value = "NEW", target = "java/util/Random"))
    private Random redirectRandomTickRandom() {
        return new XoRoShiRoRandom();
    }

}
