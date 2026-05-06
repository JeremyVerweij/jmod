package com.jmod.mixin.jrender.core.pipeline;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UnpackedBakedQuad.class)
public class MixinUnpackedBakedQuad {
    protected int cachedFlags;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void calculateFlags(CallbackInfo ci) {
        this.cachedFlags = ModelQuadFlags.getQuadFlags((BakedQuad) (Object) this);
    }
}
