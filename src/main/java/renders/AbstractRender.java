package renders;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

public abstract class AbstractRender {
    private final Set<Setting> settings;
    private final int allowedWith;
    private final int allowedHeight;
    private final Logger logger;

    public AbstractRender(Set<Setting> settings, int allowedWith, int allowedHeight, Logger logger) {
        this.settings = settings;
        this.allowedWith = allowedWith;
        this.allowedHeight = allowedHeight;
        this.logger = logger;
    }

    public abstract Image renderImage(BufferedImage image, Graphics2D graphics);

    public int getAllowedWith() {
        return allowedWith;
    }

    public int getAllowedHeight() {
        return allowedHeight;
    }

    public Set<Setting> getSettings() {
        return settings;
    }

    @Nullable
    public Setting getSetting(Setting.Type type) {
        for (Setting setting : getSettings()) {
            if (setting.getType() == type) {
                return setting;
            }
        }

        return null;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean hasSetting(Setting.Type type) {
        for (Setting setting : getSettings()) {
            if (setting.getType() == type) {
                return true;
            }
        }

        return false;
    }
}
