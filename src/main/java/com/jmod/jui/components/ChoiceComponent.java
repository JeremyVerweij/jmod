package com.jmod.jui.components;

import com.jmod.jui.ui.interfaces.DoubleInputConsumer;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ChoiceComponent extends ButtonComponent{
    protected int selectedChildIndex = 0;
    protected DoubleInputConsumer<ChoiceComponent, String> onChoiceChange;

    public ChoiceComponent(String id, Minecraft mc) {
        super(id, mc);
    }

    @Override
    protected String getTranslatedText() {
        return this.children.get(this.selectedChildIndex).translationKey;
    }

    @Override
    public void onMouseClick(int button, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.onMouseClick(button, offsetX, offsetY, mouseX, mouseY);

        this.selectedChildIndex += Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? -1 : 1;

        if (this.selectedChildIndex >= this.children.size()) this.selectedChildIndex = 0;
        if (this.selectedChildIndex < 0) this.selectedChildIndex = this.children.size() - 1;

        if(this.onChoiceChange != null) this.onChoiceChange.apply(this, this.getTranslatedText());
    }

    public void setOnChoiceChange(DoubleInputConsumer<ChoiceComponent, String> onChoiceChange) {
        this.onChoiceChange = onChoiceChange;
    }
}
