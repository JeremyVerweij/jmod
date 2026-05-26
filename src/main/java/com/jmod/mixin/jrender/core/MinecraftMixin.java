package com.jmod.mixin.jrender.core;

import com.jmod.jrender.client.render.world.JRenderGlobal;
import com.jmod.jrender.client.overlay.JRenderGuiInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Redirect(method = "init",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraftforge/client/GuiIngameForge;"
            )
    )
    private GuiIngameForge createInGameGui(Minecraft mc){
        return new JRenderGuiInGame(mc);
    }

    @Redirect(method = "init",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraft/client/renderer/RenderGlobal;"
            )
    )
    private RenderGlobal createRenderGlobal(Minecraft mcIn){
        return new JRenderGlobal(mcIn);
    }
}
