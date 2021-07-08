package renders;

import java.util.Objects;

public class Setting {
    private final Type type;
    private Object value;

    public Setting(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
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
