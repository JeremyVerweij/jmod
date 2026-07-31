package com.jmod.jqol.settings;

import com.jmod.jqol.JQol;
import com.jmod.jui.components.list.ListComponent;
import com.jmod.jui.components.list.VerticalListCategorizedComponent;
import com.jmod.jui.ui.JUIScreen;
import com.jmod.jui.ui.UIDocument;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.*;

public class JControlSettings extends JUIScreen {
    private final Map<KeyBinding, Set<KeyBinding>> conflicts;
    protected final GuiScreen parentScreen;
    protected final GameSettings options;

    public JControlSettings(GuiScreen screen, GameSettings settings){
        this.parentScreen = screen;
        this.options = settings;

        this.conflicts = new HashMap<>();
    }

    @Override
    protected UIDocument getUIDocument() {
        return JQol.instance.controls;
    }

    @Override
    protected String getTitleTranslationKey() {
        return "controls.title";
    }

    @Override
    protected void initJUI(UIDocument document) {
        this.reloadAllConflicts();

        System.out.println(this.conflicts);

        VerticalListCategorizedComponent list = document.getComponent("test");
        KeyBindComponent btn = document.getComponent("keyBind");

        for (KeyBinding keyBinding : this.options.keyBindings) {
            list.addEntry(keyBinding.getKeyCategory(), keyBinding::getKeyDescription, () -> {
                if(this.conflicts.getOrDefault(keyBinding, Collections.EMPTY_SET).isEmpty()){
                    return keyBinding.getDisplayName();
                }else{
                    return "§c" + keyBinding.getDisplayName();
                }
            });
        }

        btn.setOnKeyBindInput((comp, event) -> {
            ListComponent.ListEntry entry = list.getEntry(list.lastCategoryInteracted, list.lastIndexInteract);
            KeyBinding keyBinding = getKeyBinding(entry);

            if (keyBinding == null) return;

            System.out.println(event);
        });
    }

    private KeyBinding getKeyBinding(ListComponent.ListEntry entry){
        String componentText = entry.getProviders()[0].getTranslationKey();
        KeyBinding keyBinding = null;

        for (KeyBinding binding : this.options.keyBindings) {
            if (binding.getKeyDescription().equals(componentText)){
                keyBinding = binding;
            }
        }

        return keyBinding;
    }

    @Override
    protected GuiScreen parentScreen() {
        return this.parentScreen;
    }

    private void reloadAllConflicts(){
        this.conflicts.clear();

        for (KeyBinding keyBinding : this.options.keyBindings) {
            this.conflicts.put(keyBinding, new HashSet<>());
        }

        for (int i = 0; i < this.options.keyBindings.length; i++) {
            for (int j = i + 1; j < this.options.keyBindings.length; j++) {
                KeyBinding a = this.options.keyBindings[i];
                KeyBinding b = this.options.keyBindings[j];

                Set<KeyBinding> conflictA = this.conflicts.get(a);
                Set<KeyBinding> conflictB = this.conflicts.get(b);

                if ((a.conflicts(b) || b.conflicts(a)) && a.getKeyCode() != Keyboard.KEY_NONE){
                    conflictA.add(b);
                    conflictB.add(a);
                }
            }
        }
    }

    private void reloadConflictsForKeybind(KeyBinding keyBinding){
        Set<KeyBinding> conflict = this.conflicts.get(keyBinding);

        for (KeyBinding other : conflict) {
            this.conflicts.get(other).remove(other);
        }

        conflict.clear();

        for (KeyBinding other : this.options.keyBindings) {
            if (other == keyBinding) continue;

            Set<KeyBinding> otherConflict = this.conflicts.get(other);

            if ((keyBinding.conflicts(other) || other.conflicts(keyBinding)) && keyBinding.getKeyCode() != Keyboard.KEY_NONE){
                otherConflict.add(keyBinding);
                conflict.add(other);
            }
        }
    }
}
