package com.jmod.jqol.settings;

import com.jmod.jui.components.basic.ButtonComponent;
import com.jmod.jui.ui.interfaces.DoubleInputConsumer;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class KeyBindComponent extends ButtonComponent {
    protected DoubleInputConsumer<KeyBindComponent, KeybindEvent> onKeyBindInput = (a, b) -> {};
    protected int keyDown = -1;
    protected boolean shift, alt, ctrl;

    public KeyBindComponent(String id, Minecraft mc) {
        super(id, mc);
        this.enableHighlight = true;
        this.centerText = true;
    }

    @Override
    public void draw(int left, int top, int mouseX, int mouseY) {
        super.draw(left, top, mouseX, mouseY);

        if (this.keyDown != -1 && !Keyboard.isKeyDown(this.keyDown)) {
            applyKeyChange();
        }
    }

    private void applyKeyChange() {
        this.onKeyBindInput.apply(this, new KeybindEvent(this.keyDown, this.shift, this.ctrl, this.alt));
        this.keyDown = -1;
        this.shift = false;
        this.ctrl = false;
        this.alt = false;
        this.owner.setFocussed(null);
    }

    @Override
    public void onMouseClick(int button, int offsetX, int offsetY, int mouseX, int mouseY) {
        if (this.hasFocus()){
            boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
            boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
            boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);

            if (this.keyDown == -1 || this.isModKey(this.keyDown)){
                this.keyDown = button - 100;
                this.shift = shift;
                this.alt = alt;
                this.ctrl = ctrl;

                applyKeyChange();
            }
        }else super.onMouseClick(button, offsetX, offsetY, mouseX, mouseY);
    }

    @Override
    public boolean onKeyType(char character, int key, boolean shift, boolean ctrl, boolean alt, int offsetX, int offsetY, int mouseX, int mouseY) {
        if (this.hasFocus()){
            if (key == Keyboard.KEY_ESCAPE){
                this.owner.setFocussed(null);
                this.onKeyBindInput.apply(this, new KeybindEvent(0, false, false, false));
            } else if (this.isModKey(key) && this.keyDown == -1) {
                this.keyDown = key;
                this.shift = shift;
                this.alt = alt;
                this.ctrl = ctrl;
            } else if (this.keyDown == -1 || this.isModKey(this.keyDown)) {
                this.keyDown = key;
                this.shift = shift;
                this.alt = alt;
                this.ctrl = ctrl;
            }

            return true;
        }

        return super.onKeyType(character, key, shift, ctrl, alt, offsetX, offsetY, mouseX, mouseY);
    }

    private boolean isModKey(int key){
        return key == Keyboard.KEY_LSHIFT || key == Keyboard.KEY_RSHIFT ||
                key == Keyboard.KEY_LCONTROL || key == Keyboard.KEY_RCONTROL ||
                key == Keyboard.KEY_LMENU || key == Keyboard.KEY_RMENU;
    }

    @Override
    public boolean grabFocus() {
        return true;
    }

    public void setOnKeyBindInput(DoubleInputConsumer<KeyBindComponent, KeybindEvent> onKeyBindInput) {
        this.onKeyBindInput = onKeyBindInput;
    }

    public record KeybindEvent(int keyCode, boolean shift, boolean ctrl, boolean alt){}
}
