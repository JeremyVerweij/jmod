package com.jmod.jui.components.basic;

import com.jmod.jui.ui.UIDocument;
import com.jmod.jui.ui.interfaces.DoubleInputConsumer;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.Objects;
import java.util.Set;

public class TextInputComponent extends ButtonComponent{
    private static final Set<Integer> ALLOW_KEYS = Set.of(
            Keyboard.KEY_A, Keyboard.KEY_B, Keyboard.KEY_C, Keyboard.KEY_D, Keyboard.KEY_E, Keyboard.KEY_F, Keyboard.KEY_G,
            Keyboard.KEY_H, Keyboard.KEY_I, Keyboard.KEY_J, Keyboard.KEY_K, Keyboard.KEY_L, Keyboard.KEY_M, Keyboard.KEY_N,
            Keyboard.KEY_O, Keyboard.KEY_P, Keyboard.KEY_Q, Keyboard.KEY_R, Keyboard.KEY_S, Keyboard.KEY_T, Keyboard.KEY_U,
            Keyboard.KEY_V, Keyboard.KEY_W, Keyboard.KEY_X, Keyboard.KEY_Y, Keyboard.KEY_Z, Keyboard.KEY_0, Keyboard.KEY_1,
            Keyboard.KEY_2, Keyboard.KEY_3, Keyboard.KEY_4, Keyboard.KEY_5, Keyboard.KEY_6, Keyboard.KEY_7, Keyboard.KEY_8,
            Keyboard.KEY_9, Keyboard.KEY_LBRACKET, Keyboard.KEY_RBRACKET, Keyboard.KEY_ADD, Keyboard.KEY_MINUS, Keyboard.KEY_SEMICOLON,
            Keyboard.KEY_APOSTROPHE, Keyboard.KEY_COMMA, Keyboard.KEY_SLASH, Keyboard.KEY_PERIOD, Keyboard.KEY_SPACE, Keyboard.KEY_UNDERLINE,
            Keyboard.KEY_EQUALS, Keyboard.KEY_COLON, Keyboard.KEY_BACKSLASH, Keyboard.KEY_AT, Keyboard.KEY_POWER
    );

    protected StringBuilder textField = new StringBuilder();
    protected int insertPosition = 0;
    protected int carrotColor = 0;
    protected DoubleInputConsumer<TextInputComponent, String> onTextChange = null;

    public TextInputComponent(String id, Minecraft mc) {
        super(id, mc);
        this.centerText = false;
    }

    @Override
    public boolean grabFocus() {
        return true;
    }

    @Override
    protected boolean isHover(int left, int top, int mouseX, int mouseY) {
        return super.isHover(left, top, mouseX, mouseY) || this.hasFocus();
    }

    @Override
    protected void drawForeground(int x, int y, boolean isHover) {
        this.enableScissors(x + this.defaultTextPadding(), y,
                this.width - this.defaultTextPadding() - this.defaultTextPadding(), this.height);

        super.drawForeground(x, y, isHover);

        if (this.hasFocus() && (((System.currentTimeMillis() >> 9) & 1) == 0)){
            int fontHeight = this.mc.fontRenderer.FONT_HEIGHT;
            x += this.mc.fontRenderer.getStringWidth(this.getTranslatedText().substring(0, this.insertPosition)) + textXOffset();
            y += (this.height - fontHeight) / 2;

            this.drawRect(x, y, x + fontHeight / 2, y + fontHeight, this.carrotColor);
        }

        this.disableScissors();
    }

    @Override
    public boolean onKeyType(char character, int key, boolean shift, boolean ctrl, boolean alt, int offsetX, int offsetY, int mouseX, int mouseY) {
        if(super.onKeyType(character, key, shift, ctrl, alt, offsetX, offsetY, mouseX, mouseY)) return true;

        if (this.hasFocus()){
            if (key == Keyboard.KEY_UP || key == Keyboard.KEY_DOWN || ctrl || alt) {
                return false;
            } else if (key == Keyboard.KEY_ESCAPE) {
                this.owner.setFocussed(null);
            } else if (key == Keyboard.KEY_LEFT) {
                if (this.insertPosition > 0){
                    this.insertPosition--;
                }
            } else if (key == Keyboard.KEY_RIGHT) {
                if (this.insertPosition < this.textField.length()){
                    this.insertPosition++;
                }
            } else if (key == Keyboard.KEY_BACK){
                if (this.insertPosition > 0){
                    this.textField.delete(this.insertPosition - 1, this.insertPosition);
                    this.insertPosition--;
                    this.runOnTextChange();
                }
            } else if (key == Keyboard.KEY_DELETE) {
                if (this.insertPosition < this.textField.length()){
                    this.textField.delete(this.insertPosition, this.insertPosition + 1);
                    this.runOnTextChange();
                }
            }else if (ALLOW_KEYS.contains(key)){
                this.textField.insert(this.insertPosition++, character);
                this.runOnTextChange();
            }
        }

        return false;
    }

    @Override
    public void onMouseClick(int button, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.onMouseClick(button, offsetX, offsetY, mouseX, mouseY);

        if (this.hasFocus() && !this.isInBoundingBox(offsetX, offsetY, mouseX, mouseY)){
            this.owner.setFocussed(null);
        }

        if (this.isInBoundingBox(offsetX, offsetY, mouseX, mouseY) && button == 1){
            this.textField.delete(0, this.textField.length());
            this.insertPosition = 0;
        }
    }

    @Override
    protected String getTranslatedText() {
        return this.textField.toString();
    }

    @Override
    public void addExtraAttribute(String key, String value) {
        super.addExtraAttribute(key, value);

        if (Objects.equals(key, "carrotColor")){
            this.carrotColor = UIDocument.parseColor(value);
        }
    }

    @Override
    protected int textXOffset() {
        return this.defaultTextPadding() + Math.min(0,
                (this.width - defaultTextPadding() - defaultTextPadding()) -
                        (this.mc.fontRenderer.getStringWidth(this.getTranslatedText().substring(0, this.insertPosition)) +
                                (this.mc.fontRenderer.FONT_HEIGHT / 2)));
    }

    protected int defaultTextPadding(){
        return 4;
    }

    private void runOnTextChange(){
        if (this.onTextChange != null) this.onTextChange.apply(this, this.getTranslatedText());
    }

    public void setOnTextChange(DoubleInputConsumer<TextInputComponent, String> onTextChange) {
        this.onTextChange = onTextChange;
    }
}
