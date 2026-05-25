package com.jmod.mixin.jrender.core;

import com.jmod.jrender.client.render.world.JRenderGlobal;
import com.jmod.jrender.client.render.entity.JRenderEntityRenderer;
import com.jmod.jrender.client.overlay.JRenderGuiInGame;
import com.jmod.jrender.client.render.world.block.JRenderBlockRenderDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.color.BlockColors;
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

    @Redirect(method = "init",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraft/client/renderer/RenderGlobal;"
            )
    )
    private RenderGlobal createRenderGlobal(Minecraft mcIn){
        return new JRenderGlobal(mcIn);
    }

    @Redirect(method = "init",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;"
            )
    )
    private BlockRendererDispatcher createBlockRenderDispatcher(BlockModelShapes p_i46577_1_, BlockColors p_i46577_2_){
        return new JRenderBlockRenderDispatcher(p_i46577_1_, p_i46577_2_);
    }
}
