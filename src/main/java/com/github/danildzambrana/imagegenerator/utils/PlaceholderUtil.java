package com.github.danildzambrana.imagegenerator.utils;

import java.util.Map;

public class PlaceholderUtil {
    private Map<String, String> placeholders;

    public PlaceholderUtil(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    public String replacePlaceholders(String text) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            text.replace(key, value);
        }

        return text;
    }
}
