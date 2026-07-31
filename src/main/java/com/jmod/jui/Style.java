package com.jmod.jui;

import java.util.HashMap;
import java.util.Map;

public class Style {
    public static final Map<String, Style> STYLES = new HashMap<>();

    static {
        STYLES.put("Default", new Style(
                "backgroundColor", "#FF000000",
                "foregroundColor", "#FFFFFFFF",
                "highlightBackgroundColor", "#FF000000",
                "highlightForegroundColor", "#FFFFFFFF",
                "borderColor", "#FFFFFFFF",
                "carrotColor", "#88FFFFFF",
                "borderColorHover", "#FFFFFFFF"
        ));

        STYLES.put("KeyBind", new Style(STYLES.get("Default"),
                "highlightBackgroundColor", "#FF000000",
                "highlightForegroundColor", "#FFFFFFA0",
                "borderColorHover", "#FFFFFFA0"
        ));
    }

    private final Map<String, String> attributes;

    public Style(Style parent, String... keyValues){
        this.attributes = new HashMap<>(parent.attributes);

        if (keyValues.length % 2 == 0){
            for (int i = 0; i < keyValues.length; i+=2) {
                String key = keyValues[i];
                String value = keyValues[i + 1];
                this.attributes.put(key, value);
            }
        }
    }

    public Style(String... keyValues){
        this.attributes = new HashMap<>();

        if (keyValues.length % 2 == 0){
            for (int i = 0; i < keyValues.length; i+=2) {
                String key = keyValues[i];
                String value = keyValues[i + 1];
                this.attributes.put(key, value);
            }
        }
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
