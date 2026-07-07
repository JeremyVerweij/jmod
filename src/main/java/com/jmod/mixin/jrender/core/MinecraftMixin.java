package com.jmod.mixin.jrender.core;

import com.jmod.jrender.client.debug.ExtendedDebug;
import com.jmod.jrender.client.render.world.JRenderGlobal;
import com.jmod.jrender.client.overlay.JRenderGuiInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public RenderGlobal renderGlobal;

    @Shadow
    protected abstract void debugFeedbackTranslated(String untranslatedTemplate, Object... objs);

    @Shadow
    private RenderManager renderManager;
    @Shadow
    public GuiIngame ingameGUI;
    @Shadow
    public GameSettings gameSettings;
    @Shadow
    public DebugRenderer debugRenderer;
    @Shadow
    public EntityPlayerSP player;

    @Shadow
    @Deprecated
    public abstract void refreshResources();

    @Redirect(method = "init",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraftforge/client/GuiIngameForge;"
            )
    )
    private GuiIngameForge createInGameGui(Minecraft mc){
        return new JRenderGuiInGame(mc);
    }

//    @Redirect(method = "init",
//            at = @At(
//                    value = "NEW",
//                    target = "Lnet/minecraft/client/renderer/RenderGlobal;"
//            )
//    )
//    private RenderGlobal createRenderGlobal(Minecraft mcIn){
//        return new JRenderGlobal(mcIn);
//    }

    /**
     * @author jrender
     * @reason needed more f3 keys for debugging
     */
    @Overwrite
    private boolean processKeyF3(int auxKey) {
        if (auxKey == 30) {
            this.renderGlobal.loadRenderers();
            this.debugFeedbackTranslated("debug.reload_chunks.message");
            return true;
        } else if (auxKey == 48) {
            boolean flag1 = !this.renderManager.isDebugBoundingBox();
            this.renderManager.setDebugBoundingBox(flag1);
            this.debugFeedbackTranslated(flag1 ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
            return true;
        } else if (auxKey == 32) {
            if (this.ingameGUI != null) {
                this.ingameGUI.getChatGUI().clearChatMessages(false);
            }

            return true;
        } else if (auxKey == 33) {
            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
            this.debugFeedbackTranslated("debug.cycle_renderdistance.message", this.gameSettings.renderDistanceChunks);
            return true;
        } else if (auxKey == 34) {
            boolean flag = this.debugRenderer.toggleChunkBorders();
            this.debugFeedbackTranslated(flag ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
            return true;
        } else if (auxKey == 35) {
            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
            this.debugFeedbackTranslated(this.gameSettings.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
            this.gameSettings.saveOptions();
            return true;
        } else if (auxKey == 49) {
            if (!this.player.canUseCommand(2, "")) {
                this.debugFeedbackTranslated("debug.creative_spectator.error");
            } else if (this.player.isCreative()) {
                this.player.sendChatMessage("/gamemode spectator");
            } else if (this.player.isSpectator()) {
                this.player.sendChatMessage("/gamemode creative");
            }

            return true;
        } else if (auxKey == 25) {
            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
            this.gameSettings.saveOptions();
            this.debugFeedbackTranslated(this.gameSettings.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
            return true;
        } else if (auxKey == 16) {
            this.debugFeedbackTranslated("debug.help.message");
            GuiNewChat guinewchat = this.ingameGUI.getChatGUI();
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_chunks.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.show_hitboxes.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.clear_chat.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.cycle_renderdistance.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.chunk_boundaries.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.advanced_tooltips.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.creative_spectator.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.pause_focus.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.help.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_resourcepacks.help"));
            
            ExtendedDebug.getInstance().addHelpInfo(guinewchat);
            
            return true;
        } else if (auxKey == 20) {
            this.debugFeedbackTranslated("debug.reload_resourcepacks.message");
            this.refreshResources();
            return true;
        } else {
            return ExtendedDebug.getInstance().processF3Key(auxKey);
        }
    }
}
