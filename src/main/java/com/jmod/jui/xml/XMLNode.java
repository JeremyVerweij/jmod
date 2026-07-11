package com.jmod.jui.xml;

import com.jmod.jui.Style;

import java.util.*;

public class XMLNode {
    public final String type;
    private String value;
    private final Map<String, String> attributes;
    private final Collection<XMLNode> children;
    private XMLNode parent;

    public XMLNode(String type){
        this.type = type;
        this.value = "";
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void addChild(XMLNode node){
        if (node.parent == null){
            this.children.add(node);
            node.parent = this;
        }
    }

    public void removeChild(XMLNode node){
        if (this.children.contains(node)) {
            this.children.remove(node);
            node.parent = null;
        }
    }

    public Collection<XMLNode> getChildren() {
        return children;
    }

    public void addAttribute(String name, String value){
        this.attributes.put(name, value);
    }

    public String getAttribute(String name){
        return this.attributes.get(name);
    }

    public String getAttributeOrDefault(String name, String defaultV){
        return this.attributes.getOrDefault(name, defaultV);
    }

    public boolean hasAttribute(String name){
        return this.attributes.containsKey(name);
    }

    public String getAttributeOrDefaultFromStyle(String name, String defaultValue, Style style){
        return this.attributes.getOrDefault(name, style.getAttributes().getOrDefault(name, defaultValue));
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "XMLNode{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", attributes=" + attributes +
                ", children=" + children +
                '}';
    }
}
