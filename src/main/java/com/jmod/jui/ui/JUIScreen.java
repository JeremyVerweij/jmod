package com.jmod.jui.ui;

import com.jmod.jui.components.BaseComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.jspecify.annotations.NonNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public abstract class JUIScreen extends GuiScreen {
    protected BaseComponent dragging = null;
    protected BaseComponent focussed = null;
    protected int lastX = 0;
    protected int lastY = 0;

    protected abstract UIDocument getUIDocument();
    protected abstract String getTitleTranslationKey();
    protected abstract void initJUI(UIDocument document);

    protected int getTitleColor(){
        return 16777215;
    }

    protected int getLeft(){
        return (this.width - this.getUIDocument().getRoot().getWidth()) / 2;
    }

    protected int getTop(){
        return (this.height - this.getUIDocument().getRoot().getHeight()) / 2;
    }

    @Override
    public void setWorldAndResolution(@NonNull Minecraft mc, int width, int height) {
        this.getUIDocument().setOwner(this);

        boolean init = this.mc != mc;

        super.setWorldAndResolution(mc, width, height);

        if(init) this.initJUI(this.getUIDocument());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        if (this.getTitleTranslationKey() != null &&
                this.height > this.getUIDocument().getRoot().getHeight() + 24 + this.fontRenderer.FONT_HEIGHT)
            this.drawCenteredString(this.fontRenderer, I18n.format(this.getTitleTranslationKey()),
                    this.width / 2, 8, this.getTitleColor());

        this.getUIDocument().draw(getLeft(), getTop(), mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.focussed != null){
            this.focussed.onMouseClick(mouseButton, getLeft(), getTop(), mouseX, mouseY);
        }else if (this.getUIDocument().getRoot().isInBoundingBox(getLeft(), getTop(), mouseX, mouseY)) {
            this.getUIDocument().getRoot().onMouseClick(mouseButton, getLeft(), getTop(), mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        if (clickedMouseButton != 0) return;

        int dx = this.lastX - mouseX;
        int dy = this.lastY - mouseY;

        if (this.dragging != null){
            this.dragging.onMouseDrag(dx, dy, getLeft(), getTop(), mouseX, mouseY);
        }else if (this.focussed != null){
            this.focussed.onMouseDrag(dx, dy, getLeft(), getTop(), mouseX, mouseY);
        }else if (this.getUIDocument().getRoot().isInBoundingBox(getLeft(), getTop(), mouseX, mouseY)) {
            this.getUIDocument().getRoot().onMouseDrag(dx, dy, getLeft(), getTop(), mouseX, mouseY);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int wheel = Mouse.getEventDWheel();

        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        if (wheel != 0){
            if (this.focussed != null){
                this.focussed.onMouseScroll(Integer.compare(wheel, 0), getLeft(), getTop(), mouseX, mouseY);
            }else if (this.getUIDocument().getRoot().isInBoundingBox(getLeft(), getTop(), mouseX, mouseY)){
                this.getUIDocument().getRoot().onMouseScroll(Integer.compare(wheel, 0), getLeft(), getTop(), mouseX, mouseY);
            }
        }

        if(!Mouse.isButtonDown(0)) this.dragging = null;

        this.lastX = mouseX;
        this.lastY = mouseY;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
        boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU);

        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        if (this.getFocussed() != null) {
            this.getFocussed().onKeyType(typedChar, keyCode, shift, ctrl, alt, getLeft(), getTop(), mouseX, mouseY);
        }else if (this.getUIDocument().getRoot().isInBoundingBox(getLeft(), getTop(), mouseX, mouseY)){
            this.getUIDocument().getRoot().onKeyType(typedChar, keyCode, shift, ctrl, alt, getLeft(), getTop(), mouseX, mouseY);
        }
    }

    public BaseComponent getFocussed() {
        return focussed;
    }

    public void setFocussed(BaseComponent focussed) {
        if (this.focussed != null){
            this.focussed.onFocusLost();
        }

        this.focussed = focussed;
        if(this.focussed != null) this.focussed.onFocusGained();
    }

    public void setDragging(BaseComponent component){
        this.dragging = component;
    }
}
