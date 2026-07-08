package com.jmod.jqol.settings;

import com.jmod.jui.components.JUICustomButton;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.jspecify.annotations.NonNull;

public class JControlSettings extends GuiControls {
    private static final int DONE_BUTTON_ID = 200;
    private static final int RESET_BUTTON_ID = 201;

    public JControlSettings(GuiScreen screen, GameSettings settings) {
        super(screen, settings);
    }

    @Override
    public void initGui() {
//        this.keyBindingList = new GuiKeyBindingList(this, this.mc);

        this.buttonList.add(new JUICustomButton(DONE_BUTTON_ID,
                this.width / 2 - 155 + 160, this.height - 29, 150, 20,
                I18n.format("gui.done")));
        this.buttonReset = this.addButton(new JUICustomButton(RESET_BUTTON_ID,
                this.width / 2 - 155, this.height - 29, 150, 20,
                I18n.format("controls.resetAll")));
        this.screenTitle = I18n.format("controls.title");
//        int i = 0;
//
//        for (GameSettings.Options gamesettings$options : OPTIONS_ARR)
//        {
//            if (gamesettings$options.isFloat())
//            {
//                this.buttonList.add(new GuiOptionSlider(gamesettings$options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options));
//            }
//            else
//            {
//                this.buttonList.add(new GuiOptionButton(gamesettings$options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.options.getKeyBinding(gamesettings$options)));
//            }
//
//            ++i;
//        }
    }

    @Override
    protected void actionPerformed(@NonNull GuiButton button) {
        if (button.id == DONE_BUTTON_ID)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == RESET_BUTTON_ID)
        {
            for (KeyBinding keybinding : this.mc.gameSettings.keyBindings)
            {
                keybinding.setToDefault();
            }

            KeyBinding.resetKeyBindingArrayAndHash();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void handleMouseInput() {
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);

        for (GuiButton guiButton : this.buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (GuiLabel guiLabel : this.labelList) {
            guiLabel.drawLabel(this.mc, mouseX, mouseY);
        }
    }
}
