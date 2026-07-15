package com.jmod.jui.components.list;

import com.jmod.jui.components.BaseComponent;
import com.jmod.jui.ui.interfaces.ITranslatorProvider;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerticalListCategorizedComponent extends ListComponent {
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
            String category = categorizedEntry.getKey();

            this.drawCenteredString(this.mc.fontRenderer, category, this.getX(left) + (this.width / 2),
                    this.getY(top + offset), this.foregroundColor);

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
