package renders;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractRender {
    private final Set<Setting> settings;
    private final int width;
    private final int height;
    private final Logger logger;

    public AbstractRender(Set<Setting> settings, int width, int height, Logger logger) {
        this.settings = settings;
        this.width = width;
        this.height = height;
        this.logger = logger;
    }

    public abstract Image renderImage(BufferedImage image, Graphics2D graphics);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Set<Setting> getSettings() {
        return settings;
    }

    public Optional<Setting> getSetting(Setting.Type type) {
        for (Setting setting : getSettings()) {
            if (setting.getType() == type) {
                return Optional.of(setting);
            }
        }

        return Optional.empty();
    }

    public boolean hasSetting(Setting.Type type) {
        for (Setting setting : getSettings()) {
            if (setting.getType() == type) {
                return true;
            }
        }

        return false;
    }

    public void addSetting(Setting.Type type, Object value) {
        getSettings().add(new Setting(type, value));
    }

    public Logger getLogger() {
        return logger;
    }
}
