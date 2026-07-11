package com.jmod.jui.ui;

import com.jmod.jui.Style;
import com.jmod.jui.components.BaseComponent;
import com.jmod.jui.components.UIComponent;
import com.jmod.jui.xml.XMLNode;
import com.jmod.jui.xml.XMLUtils;
import net.minecraft.client.Minecraft;

import java.util.*;

public class UIDocument {
    private final Map<String, BaseComponent> components;
    private final UIComponent root;

    public UIDocument(XMLNode root, Minecraft minecraft){
        this.components = new HashMap<>();

        this.addNodeToComponents(root, null, minecraft, Style.STYLES.get("Default"));

        this.root = getComponentsByType(UIComponent.class).getFirst();
    }

    public UIDocument(String internalPath, Minecraft minecraft){
        this(XMLUtils.createNodeTree(XMLUtils.createXMLStream(UIDocument.class.getResourceAsStream(internalPath))), minecraft);
    }

    public <T extends BaseComponent> T getComponent(String id){
        //noinspection unchecked
        return (T) this.components.get(id);
    }

    public <T extends BaseComponent> List<T> getComponentsByType(Class<T> componentClass){
        //noinspection unchecked
        return (List<T>) this.components.values().stream().filter((a) -> a.getClass() == componentClass).toList();
    }

    public void setOwner(JUIScreen owner){
        for (BaseComponent value : this.components.values()) {
            value.setOwner(owner);
        }
    }

    public void draw(int left, int top, int mouseX, int mouseY){
        this.root.draw(left, top, mouseX, mouseY);
    }

    public void addComponent(BaseComponent parent, BaseComponent component){
        parent.addChild(component);
        this.components.put(component.getId(), component);
    }

    public void removeComponent(BaseComponent component){
        if (component.getParent() != null){
            component.getParent().removeChild(component);
        }

        this.components.remove(component.getId());
    }

    private void addNodeToComponents(XMLNode node, BaseComponent parent, Minecraft minecraft, Style style){
        int xO = 0, yO = 0, wO = 0, hO = 0;

        if (parent != null){
            xO = parent.getDummyX();
            yO = parent.getDummyY();
            wO = parent.getWidth();
            hO = parent.getHeight();
        }

        style = node.hasAttribute("style") ? Style.STYLES.get(node.getAttribute("style")) : style;

        int x = calcWithRelativeInMind(node.getAttributeOrDefault("x", "0"), wO, xO);
        int y = calcWithRelativeInMind(node.getAttributeOrDefault("y", "0"), hO, yO);
        int width = calcWithRelativeInMind(node.getAttributeOrDefault("width", "0"), wO, 0);
        int height = calcWithRelativeInMind(node.getAttributeOrDefault("height", "0"), hO, 0);
        int backgroundColor = parseColor(node.getAttributeOrDefaultFromStyle("backgroundColor", "rgba(0, 0, 0, 0)", style));
        int foregroundColor = parseColor(node.getAttributeOrDefaultFromStyle("foregroundColor", "rgba(0, 0, 0, 0)", style));
        int highlightBackgroundColor = parseColor(node.getAttributeOrDefaultFromStyle("highlightBackgroundColor", "int(" + backgroundColor + ")", style));
        int highlightForegroundColor = parseColor(node.getAttributeOrDefaultFromStyle("highlightForegroundColor", "int(" + foregroundColor + ")", style));
        String id = node.getAttributeOrDefault("id", "undefined-" + (long) (Long.MAX_VALUE * Math.random()));
        String translationKey = node.getValue();

        String xAlign = node.getAttributeOrDefault("xAlign", "left");
        String yAlign = node.getAttributeOrDefault("xAlign", "top");

        if (Objects.equals(xAlign, "right")){
            x += wO - width;
        }

        if (Objects.equals(yAlign, "bottom")){
            y += hO - height;
        }

        BaseComponent component = UIComponentCollection.createNewComponent(node.type, id, minecraft);
        component.setDummyX(x);
        component.setDummyY(y);
        component.setWidth(width);
        component.setHeight(height);
        component.setBackgroundColor(backgroundColor);
        component.setForegroundColor(foregroundColor);
        component.setHighlightBackgroundColor(highlightBackgroundColor);
        component.setHighlightForegroundColor(highlightForegroundColor);
        component.setTranslationKey(translationKey);

        this.components.put(id, component);

        if (parent != null) parent.addChild(component);

        Map<String, String> attributesCombined = new HashMap<>();
        attributesCombined.putAll(style.getAttributes());
        attributesCombined.putAll(node.getAttributes());

        for (Map.Entry<String, String> attribute : attributesCombined.entrySet()) {
            component.addExtraAttribute(attribute.getKey(), attribute.getValue());
        }

        for (XMLNode child : node.getChildren()) {
            addNodeToComponents(child, component, minecraft, style);
        }
    }

    public static int calcWithRelativeInMind(String value, int parentValue, int parentOffset){
        if (value.endsWith("%")){
            float r = Float.parseFloat(value.substring(0, value.length() - 1)) / 100f;

            return (int) (r * parentValue) + parentOffset;
        }

        return Integer.parseInt(value) + parentOffset;
    }

    public static int parseColor(String color){
        if (color.startsWith("rgba")){
            color = color.replace("rgba", "").replace("(", "").replace(")", "");
            String[] colors = color.split(",");
            float r = Float.parseFloat(colors[0].trim());
            float g = Float.parseFloat(colors[1].trim());
            float b = Float.parseFloat(colors[2].trim());
            float a = Float.parseFloat(colors[3].trim());

            return ((int) (a * Byte.MIN_VALUE) << 24) | ((int) (r * Byte.MIN_VALUE) << 16)
                    | ((int) (g * Byte.MIN_VALUE) << 8) | ((int) (b * Byte.MIN_VALUE));
        } else if (color.startsWith("int")) {
            color = color.replace("int", "").replace("(", "").replace(")", "");
            return Integer.parseInt(color);
        } else if (color.startsWith("#")){
            return Integer.parseUnsignedInt(color.substring(1), 16);
        }

        return 0;
    }

    public UIComponent getRoot() {
        return root;
    }
}
