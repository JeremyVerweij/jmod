package com.jmod.mixin.jrender.core;

import com.jmod.jrender.JRenderEntityRenderer;
import com.jmod.jrender.overlay.JRenderGuiInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Redirect(method = "init",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraft/client/renderer/EntityRenderer;"
            )
    )
    private EntityRenderer createEntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn){
        return new JRenderEntityRenderer(mcIn, resourceManagerIn);
    }

    @Redirect(method = "init",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraftforge/client/GuiIngameForge;"
            )
    )
    private GuiIngameForge createInGameGui(Minecraft mc){
        return new JRenderGuiInGame(mc);
    }
}
