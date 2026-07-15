package com.jmod.jqol.settings;

import com.jmod.jui.components.VerticalListComponent;
import com.jmod.jui.proxy.ClientProxy;
import com.jmod.jui.ui.JUIScreen;
import com.jmod.jui.ui.UIDocument;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

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
        return ClientProxy.getClientProxy().controls;
    }

    @Override
    protected String getTitleTranslationKey() {
        return "controls.title";
    }

    @Override
    protected void initJUI(UIDocument document) {
        this.reloadAllConflicts();

        System.out.println(this.conflicts);

        VerticalListComponent comp = document.getComponent("test");

        for (KeyBinding keyBinding : this.options.keyBindings) {
            comp.addEntry(keyBinding::getKeyDescription, () -> {
                if(this.conflicts.getOrDefault(keyBinding, Collections.EMPTY_SET).isEmpty()){
                    return keyBinding.getDisplayName();
                }else{
                    return "[!CONFLICT] " + keyBinding.getDisplayName();
                }
            });
        }
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

                if (a.conflicts(b) || b.conflicts(a)){
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

            if (keyBinding.conflicts(other) || other.conflicts(keyBinding)){
                otherConflict.add(keyBinding);
                conflict.add(other);
            }
        }
    }
}
