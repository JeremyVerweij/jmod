package com.jmod.jqol.settings;

import com.jmod.jui.components.JUICustomButton;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import org.jspecify.annotations.NonNull;

public class JGuiOptions extends GuiOptions {
    private static final int SKIN_BUTTON_ID = 110;
    private static final int SOUNDS_BUTTON_ID = 106;
    private static final int VIDEO_BUTTON_ID = 101;
    private static final int CONTROLS_BUTTON_ID = 100;
    private static final int LANGUAGE_BUTTON_ID = 102;
    private static final int CHAT_BUTTON_ID = 103;
    private static final int RESOURCE_PACK_BUTTON_ID = 105;
    private static final int SNOOPER_BUTTON_ID = 104;
    private static final int DONE_BUTTON_ID = 200;

    public JGuiOptions(GuiScreen screen, GameSettings settings) {
        super(screen, settings);
    }

    @Override
    public void initGui() {
        this.title = I18n.format("options.title");
        int i = 0;

        for (GameSettings.Options gamesettings$options : SCREEN_OPTIONS)
        {
            if (gamesettings$options.isFloat())
            {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options));
            }
            else
            {
                GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options, this.settings.getKeyBinding(gamesettings$options));
                this.buttonList.add(guioptionbutton);
            }

            ++i;
        }

        if (this.mc.world != null)
        {
            EnumDifficulty enumdifficulty = this.mc.world.getDifficulty();
            this.difficultyButton = new GuiButton(108, this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), 150, 20, this.getDifficultyText(enumdifficulty));
            this.buttonList.add(this.difficultyButton);

            if (this.mc.isSingleplayer() && !this.mc.world.getWorldInfo().isHardcoreModeEnabled())
            {
                this.difficultyButton.setWidth(this.difficultyButton.getButtonWidth() - 20);
                this.lockButton = new GuiLockIconButton(109, this.difficultyButton.x + this.difficultyButton.getButtonWidth(), this.difficultyButton.y);
                this.buttonList.add(this.lockButton);
                this.lockButton.setLocked(this.mc.world.getWorldInfo().isDifficultyLocked());
                this.lockButton.enabled = !this.lockButton.isLocked();
                this.difficultyButton.enabled = !this.lockButton.isLocked();
            }
            else
            {
                this.difficultyButton.enabled = false;
            }
        }
        else
        {
            this.buttonList.add(new GuiOptionButton(GameSettings.Options.REALMS_NOTIFICATIONS.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), GameSettings.Options.REALMS_NOTIFICATIONS, this.settings.getKeyBinding(GameSettings.Options.REALMS_NOTIFICATIONS)));
        }

        this.buttonList.add(new JUICustomButton(SKIN_BUTTON_ID,
                this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20,
                I18n.format("options.skinCustomisation")));
        this.buttonList.add(new JUICustomButton(SOUNDS_BUTTON_ID,
                this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20,
                I18n.format("options.sounds")));
        this.buttonList.add(new JUICustomButton(VIDEO_BUTTON_ID,
                this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20,
                I18n.format("options.video")));
        this.buttonList.add(new JUICustomButton(CONTROLS_BUTTON_ID,
                this.width / 2 + 5, this.height / 6 + 72 - 6, 150, 20,
                I18n.format("options.controls")));
        this.buttonList.add(new JUICustomButton(LANGUAGE_BUTTON_ID,
                this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20,
                I18n.format("options.language")));
        this.buttonList.add(new JUICustomButton(CHAT_BUTTON_ID,
                this.width / 2 + 5, this.height / 6 + 96 - 6, 150, 20,
                I18n.format("options.chat.title")));
        this.buttonList.add(new JUICustomButton(RESOURCE_PACK_BUTTON_ID,
                this.width / 2 - 155, this.height / 6 + 120 - 6, 150, 20,
                I18n.format("options.resourcepack")));
        this.buttonList.add(new JUICustomButton(SNOOPER_BUTTON_ID,
                this.width / 2 + 5, this.height / 6 + 120 - 6, 150, 20,
                I18n.format("options.snooper.view")));
        this.buttonList.add(new JUICustomButton(DONE_BUTTON_ID,
                this.width / 2 - 100, this.height / 6 + 168,
                I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(@NonNull GuiButton button) {
        if (button.enabled)
        {
            if (button.id < CONTROLS_BUTTON_ID && button instanceof GuiOptionButton)
            {
                GameSettings.Options gamesettings$options = ((GuiOptionButton)button).getOption();
                this.settings.setOptionValue(gamesettings$options, 1);
                button.displayString = this.settings.getKeyBinding(GameSettings.Options.byOrdinal(button.id));
            }

            if (button.id == 108)
            {
                this.mc.world.getWorldInfo().setDifficulty(EnumDifficulty.byId(this.mc.world.getDifficulty().getId() + 1));
                this.difficultyButton.displayString = this.getDifficultyText(this.mc.world.getDifficulty());
            }

            if (button.id == 109)
            {
                this.mc.displayGuiScreen(new GuiYesNo(this, (new TextComponentTranslation("difficulty.lock.title", new Object[0])).getFormattedText(), (new TextComponentTranslation("difficulty.lock.question", new Object[] {new TextComponentTranslation(this.mc.world.getWorldInfo().getDifficulty().getTranslationKey(), new Object[0])})).getFormattedText(), 109));
            }

            if (button.id == SKIN_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
            }

            if (button.id == VIDEO_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiVideoSettings(this, this.settings));
            }

            if (button.id == CONTROLS_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new JControlSettings(this, this.settings));
            }

            if (button.id == LANGUAGE_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiLanguage(this, this.settings, this.mc.getLanguageManager()));
            }

            if (button.id == CHAT_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new ScreenChatOptions(this, this.settings));
            }

            if (button.id == SNOOPER_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiSnooper(this, this.settings));
            }

            if (button.id == DONE_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.lastScreen);
            }

            if (button.id == RESOURCE_PACK_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
            }

            if (button.id == SOUNDS_BUTTON_ID)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.settings));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);

        for (GuiButton guiButton : this.buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (GuiLabel guiLabel : this.labelList) {
            guiLabel.drawLabel(this.mc, mouseX, mouseY);
        }
    }
}
