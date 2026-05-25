package com.jmod.jrender.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;

public class JRenderGuiInGame extends GuiIngameForge {
    public static final RenderGameOverlayEvent.ElementType DISABLED_DEBUG = EnumHelper.addEnum(
            RenderGameOverlayEvent.ElementType.class,
            "DISABLED_DEBUG",
            new Class<?>[0]
    );
    private final JRenderGuiOverlayDebug jRenderOverlayDebug;

    public JRenderGuiInGame(Minecraft mcIn) {
        super(mcIn);
        this.overlayDebug = this.jRenderOverlayDebug = new JRenderGuiOverlayDebug(mcIn);
    }

    protected void renderHUDText(int width, int height) {
        this.mc.profiler.startSection("forgeHudText");
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        ArrayList<String> listL = new ArrayList<>();
        ArrayList<String> listR = new ArrayList<>();
        if (this.mc.isDemo()) {
            long time = this.mc.world.getTotalWorldTime();
            if (time >= 120500L) {
                listR.add(I18n.format("demo.demoExpired"));
            } else {
                listR.add(I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - time))));
            }
        }

        if (this.mc.gameSettings.showDebugInfo) {
            if (!this.pre(RenderGameOverlayEvent.ElementType.DEBUG)){
                listL.addAll(this.jRenderOverlayDebug.getLeft());
                listR.addAll(this.jRenderOverlayDebug.getRight());
                this.post(RenderGameOverlayEvent.ElementType.DEBUG);
            }
        }else{
            if (!this.pre(DISABLED_DEBUG)) {
                listL.addAll(this.jRenderOverlayDebug.getDisabled());
                this.post(DISABLED_DEBUG);
            }
        }

        RenderGameOverlayEvent.Text event = new RenderGameOverlayEvent.Text(this.eventParent, listL, listR);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            int top = 2;

            for(String msg : listL) {
                if (msg != null) {
                    drawRect(1, top - 1, 2 + this.fontrenderer.getStringWidth(msg) + 1, top + this.fontrenderer.FONT_HEIGHT - 1, -1873784752);
                    this.fontrenderer.drawString(msg, 2, top, 14737632);
                    top += this.fontrenderer.FONT_HEIGHT;
                }
            }

            top = 2;

            for(String msg : listR) {
                if (msg != null) {
                    int w = this.fontrenderer.getStringWidth(msg);
                    int left = width - 2 - w;
                    drawRect(left - 1, top - 1, left + w + 1, top + this.fontrenderer.FONT_HEIGHT - 1, -1873784752);
                    this.fontrenderer.drawString(msg, left, top, 14737632);
                    top += this.fontrenderer.FONT_HEIGHT;
                }
            }
        }

        this.mc.profiler.endSection();
        this.post(RenderGameOverlayEvent.ElementType.TEXT);
    }
}
