package com.jmod.jui.components.list;

import com.jmod.jui.components.BaseComponent;
import com.jmod.jui.ui.interfaces.IChildModifierAcceptor;
import com.jmod.jui.ui.interfaces.ITranslatorProvider;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class ListComponent extends BaseComponent implements IChildModifierAcceptor {
    protected int increment = 1;
    protected Set<BaseComponent> useListInput;

    public ListComponent(String id, Minecraft mc) {
        super(id, mc);

        this.useListInput = new HashSet<>();
    }

    @Override
    protected void drawBackground(int x, int y, boolean isHover) {

    }

    @Override
    protected void drawForeground(int x, int y, boolean isHover) {

    }

    @Override
    public boolean grabFocus() {
        return false;
    }

    @Override
    public void addExtraAttribute(String key, String value) {
        super.addExtraAttribute(key, value);

        if (Objects.equals(key, "increment")){
            this.increment = Integer.parseInt(value);
        }
    }

    @Override
    public void addChildExtraAttrib(BaseComponent child, String key, String value) {
        if (Objects.equals(key, "useListInput")){
            if (Boolean.parseBoolean(value)){
                this.useListInput.add(child);
            }
        }
    }

    public static class ListEntry{
        protected final ITranslatorProvider[] providers;

        public ListEntry(ListComponent list, ITranslatorProvider... providers){
            if (providers.length != list.useListInput.size())
                throw new RuntimeException("Entry does not match size, Expected: " + list.useListInput.size() + " Got: " + providers.length);

            this.providers = providers;
        }
    }
}
