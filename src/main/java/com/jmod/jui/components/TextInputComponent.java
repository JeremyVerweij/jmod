package com.jmod.jui.components;

import com.jmod.jui.ui.UIDocument;
import com.jmod.jui.ui.interfaces.DoubleInputConsumer;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

public class TextInputComponent extends ButtonComponent{
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
    public void draw(int left, int top, int mouseX, int mouseY) {
        this.drawBackground(left, top, isInBoundingBox(left, top, mouseX, mouseY) || this.hasFocus());
        this.drawForeground(left, top, isInBoundingBox(left, top, mouseX, mouseY) || this.hasFocus());

        for (BaseComponent child : this.children) {
            child.draw(left, top, mouseX, mouseY);
        }
    }

    @Override
    protected void drawForeground(int left, int top, boolean isHover) {
        int x = left + this.getX();
        int y = top + this.getY();

        //TODO: ADD SCROLLING, PROBABLY HIGHJACK THE ENTIRE STRING DRAWING
        this.enableScissors(x + textXOffset(), y, this.width - textXOffset() - textXOffset(), this.height);

        super.drawForeground(left, top, isHover);

        if (this.hasFocus()){
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
            }else{
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
        return 4;
    }

    private void runOnTextChange(){
        if (this.onTextChange != null) this.onTextChange.apply(this, this.getTranslatedText());
    }

    public void setOnTextChange(DoubleInputConsumer<TextInputComponent, String> onTextChange) {
        this.onTextChange = onTextChange;
    }
}
