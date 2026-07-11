package com.jmod.jui.components;

import com.jmod.jui.ui.interfaces.ITranslatorProvider;
import net.minecraft.client.Minecraft;

import java.util.*;

public class VerticalListComponent extends BaseComponent{
    protected List<ListEntry> entries;
    protected int increment = 1;
    protected Set<BaseComponent> useListInput;

    public VerticalListComponent(String id, Minecraft mc) {
        super(id, mc);

        this.entries = new ArrayList<>();
        this.useListInput = new HashSet<>();
    }

    @Override
    protected void drawBackground(int x, int y, boolean isHover) {

    }

    @Override
    protected void drawForeground(int x, int y, boolean isHover) {

    }

    @Override
    public void draw(int left, int top, int mouseX, int mouseY) {
        int offset = 0;

        for (ListEntry entry : this.entries) {
            int i = 0;
            for (BaseComponent child : this.children) {
                if (this.useListInput.contains(child)){
                    child.setTranslatorProvider(entry.providers[i]);
                    i++;
                }

                child.draw(left, top + offset, mouseX, mouseY);
            }

            offset += this.increment;
        }
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
        super.addChildExtraAttrib(child, key, value);

        if (Objects.equals(key, "useListInput")){
            if (Boolean.parseBoolean(value)){
                this.useListInput.add(child);
            }
        }
    }

    public void addEntry(ListEntry entry){
        this.entries.add(entry);

        this.setHeight(this.entries.size() * this.increment);
    }

    public void removeEntry(ListEntry entry){
        this.entries.remove(entry);

        this.setHeight(this.entries.size() * this.increment);
    }

    public void removeAll(){
        this.entries.clear();

        this.setHeight(0);
    }

    public ListEntry getEntry(int index){
        return this.entries.get(index);
    }

    public List<ListEntry> getAll(){
        return this.entries;
    }

    public static class ListEntry{
        protected final ITranslatorProvider[] providers;

        public ListEntry(VerticalListComponent list, ITranslatorProvider... providers){
            if (providers.length != list.useListInput.size())
                throw new RuntimeException("Entry does not match size, Expected: " + list.useListInput.size() + " Got: " + providers.length);

            this.providers = providers;
        }
    }
}
