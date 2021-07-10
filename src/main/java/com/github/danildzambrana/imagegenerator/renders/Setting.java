package com.github.danildzambrana.imagegenerator.renders;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Setting {
    private final Type type;
    private Object value;

    public Setting(Type type, @NotNull Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    @NotNull
    public <V> V getValue() {
        return (V) value;
    }


    public void setValue(@NotNull Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Setting)) {
            return false;
        }
        Setting setting = (Setting) o;
        return getType() == setting.getType() && Objects.equals(getValue(), setting.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getValue());
    }

    public enum Type {
        SIZE, FONT, COLOR, STYLE, X_OFFSET, Y_OFFSET, TEXT
    }
}
