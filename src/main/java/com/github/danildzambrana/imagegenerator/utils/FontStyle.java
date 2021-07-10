package com.github.danildzambrana.imagegenerator.utils;

import java.awt.*;

public enum FontStyle {
    PLAIN(Font.PLAIN),
    BOLD(Font.BOLD),
    ITALIC(Font.ITALIC),
    BOLD_ITALIC(Font.BOLD + Font.ITALIC);

    private final int value;

    private FontStyle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
