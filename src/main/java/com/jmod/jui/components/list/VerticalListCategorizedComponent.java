package com.jmod.jui.components.list;

import com.jmod.jui.components.BaseComponent;
import com.jmod.jui.ui.interfaces.ITranslatorProvider;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerticalListCategorizedComponent extends ListComponent {
    public String lastCategoryInteracted = null;

    protected Map<String, List<ListEntry>> categorizedListEntries;
    protected Object2BooleanMap<String> shownCategories;

    public VerticalListCategorizedComponent(String id, Minecraft mc) {
        super(id, mc);

        this.categorizedListEntries = new HashMap<>();
        this.shownCategories = new Object2BooleanOpenHashMap<>();
    }

    @Override
    public void draw(int left, int top, int mouseX, int mouseY) {
        int offset = 0;

        for (Map.Entry<String, List<ListEntry>> categorizedEntry : this.categorizedListEntries.entrySet()) {
            String category = I18n.format(categorizedEntry.getKey());

            int x = this.getX(left);
            int y = this.getY(top + offset);

            this.drawRect(x, y, x + width, y + this.mc.fontRenderer.FONT_HEIGHT + 4, 0x88000000);

            this.drawCenteredString(this.mc.fontRenderer, category, x + (this.width / 2),
                    y + 2, this.foregroundColor);

            offset += this.increment;

            for (ListEntry entry : categorizedEntry.getValue()) {
                int i = 0;
                for (BaseComponent child : this.children) {
                    if (this.useListInput.contains(child)) {
                        child.setTranslatorProvider(entry.providers[i]);
                        i++;
                    }

                    child.draw(left, top + offset, mouseX, mouseY);
                }

                offset += this.increment;
            }
        }
    }

    @Override
    public void onMouseClick(int button, int offsetX, int offsetY, int mouseX, int mouseY) {
        int offset = 0;

        for (Map.Entry<String, List<ListEntry>> categorizedEntry : this.categorizedListEntries.entrySet()) {
            offset += this.increment;

            for (int index = 0; index < categorizedEntry.getValue().size(); index++) {
                for (BaseComponent child : this.children) {
                    if (child.isInBoundingBox(offsetX, offsetY + offset, mouseX, mouseY)){
                        this.lastIndexInteract = index;
                        this.lastCategoryInteracted = categorizedEntry.getKey();

                        child.onMouseClick(button, offsetX, offsetY + offset, mouseX, mouseY);
                    }
                }

                offset += this.increment;
            }
        }
    }

    @Override
    public boolean onKeyType(char character, int key, boolean shift, boolean ctrl, boolean alt, int offsetX, int offsetY, int mouseX, int mouseY) {
        int offset = 0;

        for (Map.Entry<String, List<ListEntry>> categorizedEntry : this.categorizedListEntries.entrySet()) {
            offset += this.increment;

            for (int index = 0; index < categorizedEntry.getValue().size(); index++) {
                for (BaseComponent child : this.children) {
                    if (child.isInBoundingBox(offsetX, offsetY + offset, mouseX, mouseY)){
                        this.lastCategoryInteracted = categorizedEntry.getKey();
                        this.lastIndexInteract = index;

                        if(child.onKeyType(character, key, shift, ctrl, alt, offsetX, offsetY + offset, mouseX, mouseY)) return true;
                    }
                }

                offset += this.increment;
            }
        }

        return false;
    }

    @Override
    public void onMouseScroll(int amount, int offsetX, int offsetY, int mouseX, int mouseY) {
        int offset = 0;

        for (Map.Entry<String, List<ListEntry>> categorizedEntry : this.categorizedListEntries.entrySet()) {
            offset += this.increment;

            for (int index = 0; index < categorizedEntry.getValue().size(); index++) {
                for (BaseComponent child : this.children) {
                    if (child.isInBoundingBox(offsetX, offsetY + offset, mouseX, mouseY)){
                        this.lastCategoryInteracted = categorizedEntry.getKey();
                        this.lastIndexInteract = index;

                        child.onMouseScroll(amount, offsetX, offsetY + offset, mouseX, mouseY);
                    }
                }

                offset += this.increment;
            }
        }
    }

    protected int calcSize(){
        int size = 0;

        for (List<ListEntry> value : this.categorizedListEntries.values()) {
            size += value.size() + 1;
        }

        return size * this.increment;
    }

    public void addEntry(String category, ListEntry entry){
        if (!this.categorizedListEntries.containsKey(category)) this.categorizedListEntries.put(category, new ArrayList<>());

        this.categorizedListEntries.get(category).add(entry);

        this.setHeight(this.calcSize());
    }

    public void addEntry(String category, ITranslatorProvider... providers){
        this.addEntry(category, new ListEntry(this, providers));
    }

    public void removeEntry(String category, ListEntry entry){
        this.categorizedListEntries.get(category).remove(entry);

        if (this.categorizedListEntries.get(category).isEmpty()) this.categorizedListEntries.remove(category);

        this.setHeight(this.calcSize());
    }

    public void removeAll(){
        this.categorizedListEntries.clear();

        this.setHeight(0);
    }

    public ListEntry getEntry(String category, int index){
        return this.categorizedListEntries.get(category).get(index);
    }

    public List<ListEntry> getCategory(String category){
        return this.categorizedListEntries.get(category);
    }

    public Map<String, List<ListEntry>> getAll(){
        return this.categorizedListEntries;
    }
}
