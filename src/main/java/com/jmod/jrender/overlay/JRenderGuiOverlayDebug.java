package com.jmod.jrender.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

public class JRenderGuiOverlayDebug extends GuiOverlayDebug {
    private final Minecraft mc;

    public JRenderGuiOverlayDebug(Minecraft mc) {
        super(mc);
        this.mc = mc;
    }

    @Override
    protected void renderDebugInfoLeft() {
    }

    @Override
    protected void renderDebugInfoRight(ScaledResolution scaledRes) {
    }

    public List<String> getLeft() {
        List<String> ret = this.call();
        ret.add("");
        String var10001 = this.mc.gameSettings.showDebugProfilerChart ? "visible" : "hidden";
        ret.add("Debug: Pie [shift]: " + var10001 + " FPS [alt]: " + (this.mc.gameSettings.showLagometer ? "visible" : "hidden"));
        ret.add("For help: press F3 + Q");
        return ret;
    }

    public List<String> getRight() {
        return this.getDebugInfoRight();
    }

    public List<String> getDisabled() {
        List<String> ret = new ArrayList<>();

        ret.add("Fps: " + Minecraft.getDebugFPS());

        return ret;
    }
}
