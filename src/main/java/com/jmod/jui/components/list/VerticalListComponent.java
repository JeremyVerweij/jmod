package com.jmod.jui.components.list;

import com.jmod.jui.components.BaseComponent;
import com.jmod.jui.ui.interfaces.ITranslatorProvider;
import net.minecraft.client.Minecraft;

import java.util.*;

public class VerticalListComponent extends ListComponent {
    protected List<ListEntry> entries;

    public VerticalListComponent(String id, Minecraft mc) {
        super(id, mc);

        this.entries = new ArrayList<>();
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
    public void onMouseClick(int button, int offsetX, int offsetY, int mouseX, int mouseY) {
        int offset = 0;

        for (int index = 0; index < entries.size(); index++) {
            for (BaseComponent child : this.children) {

                if (child.isInBoundingBox(offsetX, offsetY + offset, mouseX, mouseY)){
                    this.lastIndexInteract = index;

                    child.onMouseClick(button, offsetX, offsetY + offset, mouseX, mouseY);
                }
            }

            offset += this.increment;
        }
    }

    @Override
    public boolean onKeyType(char character, int key, boolean shift, boolean ctrl, boolean alt, int offsetX, int offsetY, int mouseX, int mouseY) {
        int offset = 0;

        for (int index = 0; index < entries.size(); index++) {
            for (BaseComponent child : this.children) {

                if (child.isInBoundingBox(offsetX, offsetY + offset, mouseX, mouseY)){
                    this.lastIndexInteract = index;

                    if(child.onKeyType(character, key, shift, ctrl, alt, offsetX, offsetY + offset, mouseX, mouseY)) return true;
                }
            }

            offset += this.increment;
        }

        return false;
    }

    @Override
    public void onMouseScroll(int amount, int offsetX, int offsetY, int mouseX, int mouseY) {
        int offset = 0;

        for (int index = 0; index < entries.size(); index++) {
            for (BaseComponent child : this.children) {

                if (child.isInBoundingBox(offsetX, offsetY + offset, mouseX, mouseY)){
                    this.lastIndexInteract = index;

                    child.onMouseScroll(amount, offsetX, offsetY + offset, mouseX, mouseY);
                }
            }

            offset += this.increment;
        }
    }

    public void addEntry(ListEntry entry){
        this.entries.add(entry);

        this.setHeight(this.entries.size() * this.increment);
    }

    public void addEntry(ITranslatorProvider... providers){
        this.addEntry(new ListEntry(this, providers));
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
}
