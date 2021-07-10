package com.github.danildzambrana.imagegenerator.renders;

import org.apache.log4j.Logger;
import com.github.danildzambrana.imagegenerator.utils.ConverterUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Set;

public class BackgroundColorRender extends AbstractRender{
    public BackgroundColorRender(Set<Setting> settings,
                                 int width,
                                 int height) {
        super(settings, width, height, Logger.getLogger(BackgroundColorRender.class));

        Optional<Setting> optionalColorSetting = getSetting(Setting.Type.COLOR);
        if (optionalColorSetting.isEmpty()) {
            getLogger().warn("Render of color brackground is empty. Using default Value = 0, 0, 0");
            addSetting(Setting.Type.COLOR, new Color(0, 0, 0, 255));
        } else {
            Setting setting = optionalColorSetting.get();

            if (!(setting.getValue() instanceof Color) && setting.getValue() instanceof String) {
                Optional<Color> result = ConverterUtil.convertStringToColor(setting.getValue());
                if (result.isEmpty()) {
                    addSetting(Setting.Type.COLOR, new Color(0, 0, 0, 255));
                    getLogger().warn("This value= '" + setting.getValue() + "' not is a color or not is convertible "
                            + "to color!. \n Using default value = 0, 0, 0");
                } else {
                    addSetting(Setting.Type.COLOR, result.get());
                }
            }
        }
    }

    @Override
    public Image renderImage(BufferedImage image, Graphics2D graphics) {
        Color color = getSetting(Setting.Type.COLOR).get().getValue();

        graphics.setColor(color);
        graphics.fillRect(0, 0, this.getWidth(), getHeight());
        return image;
    }
}
