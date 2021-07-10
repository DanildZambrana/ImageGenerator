package com.github.danildzambrana.imagegenerator.renders;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import com.github.danildzambrana.imagegenerator.utils.ConverterUtil;
import com.github.danildzambrana.imagegenerator.utils.DrawUtil;
import com.github.danildzambrana.imagegenerator.utils.PlaceholderUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class TextRender extends AbstractRender {
    private final PlaceholderUtil placeholderUtil;
    private final DrawUtil drawUtil;

    public TextRender(Set<Setting> settings, int width, int height, PlaceholderUtil placeholderUtil,
                      DrawUtil drawUtil) {
        super(settings, width, height, Logger.getLogger(TextRender.class));

        this.placeholderUtil = placeholderUtil;
        this.drawUtil = drawUtil;

        loadText();
        loadFont();
        loadFontSize();
        loadFontColor();
        loadFontStyle();
        loadOffset(Setting.Type.X_OFFSET);
        loadOffset(Setting.Type.Y_OFFSET);
    }

    private void loadOffset(Setting.Type offset) {
        Optional<Setting> optionalOffsetSetting = getSetting(offset);
        int position = -1;
        if (optionalOffsetSetting.isPresent()) {
            Setting setting = optionalOffsetSetting.get();
            if (setting.getValue() instanceof Integer) {
                position = setting.getValue();
            } else if (setting.getValue() instanceof String) {
                String value = setting.getValue();
                if (!value.equalsIgnoreCase("CENTERED")) {
                    getLogger().warn("Provided value is not supported, using default value CENTERED");
                }
            } else {
                getLogger().warn("Provided value is not supported, using default value CENTERED");
            }
        }
        addSetting(offset, position);
    }

    private void loadFontStyle() {
        Optional<Setting> optionalStyleSetting = getSetting(Setting.Type.STYLE);
        if (optionalStyleSetting.isEmpty()) {
            getLogger().warn("Render style of font is empty. Using default value = PLAIN");
            addSetting(Setting.Type.STYLE, Font.PLAIN);
        } else {
            Setting setting = optionalStyleSetting.get();
            if (!(setting.getValue() instanceof Integer) && setting.getValue() instanceof String) {
                Optional<Integer> result = ConverterUtil.convertStringToFontStyle(setting.getValue());

                if (result.isEmpty()) {
                    addSetting(Setting.Type.STYLE, Font.PLAIN);
                } else {
                    addSetting(Setting.Type.STYLE, result.get());
                }
            }
        }
    }

    private void loadFontColor() {
        Optional<Setting> optionalColorSetting = getSetting(Setting.Type.COLOR);
        if (optionalColorSetting.isEmpty()) {
            getLogger().warn("Render color of font is empty. Using default Value = 0, 0, 0");
            addSetting(Setting.Type.COLOR, new Color(0, 0, 0, 255));
        } else {
            Setting setting = optionalColorSetting.get();

            if (!(setting.getValue() instanceof Color) && setting.getValue() instanceof String) {
                Optional<Color> result = ConverterUtil.convertStringToColor(setting.getValue());
                if (result.isEmpty()) {
                    addSetting(Setting.Type.COLOR, new Color(255, 255, 255, 255));
                    getLogger().warn("This value= '" + setting.getValue() + "' not is a color or not is convertible "
                            + "to color!. \n Using default value = 255,255,255");
                } else {
                    addSetting(Setting.Type.COLOR, result.get());
                }
            }
        }
    }

    private void loadFontSize() {
        Optional<Setting> optionalSizeSetting = getSetting(Setting.Type.SIZE);
        if (optionalSizeSetting.isPresent()) {
            Setting setting = optionalSizeSetting.get();
            if (!(setting.getValue() instanceof Integer)) {
                getLogger().warn("The size of the text is invalid. Current value = '" + setting.getValue() + "' using"
                        + " default size. '" + 12 + "'");
                addSetting(Setting.Type.SIZE, 12);
            }
        } else {
            addSetting(Setting.Type.SIZE, 12);
        }
    }

    private void loadFont() {
        String defaultFont = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0];
        Optional<Setting> optionalFontSetting = getSetting(Setting.Type.FONT);
        if (optionalFontSetting.isPresent()) {
            Setting setting = optionalFontSetting.get();
            Optional<String> first =
                    Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
                            .filter(font -> setting.getValue().equals(font))
                            .findFirst();

            if (first.isEmpty()) {
                getLogger().warn("The specified font cannot be loaded, using default font, '" + defaultFont + "'!");
                addSetting(Setting.Type.FONT, defaultFont);
            }
        } else {
            addSetting(Setting.Type.FONT, defaultFont);
        }
    }

    private void loadText() {
        if (!hasSetting(Setting.Type.TEXT)) {
            getSettings().add(new Setting(Setting.Type.TEXT, "DEFAULT TEXT VALUE"));
        }
    }

    /**
     * Draw text in provided image.
     *
     * @param image    the image to draw the text.
     * @param graphics graphics instance to draw text.
     * @return Image with text.;
     */
    @Override
    @Nullable
    public Image renderImage(BufferedImage image, Graphics2D graphics) {
        String text = placeholderUtil.replacePlaceholders(getSetting(Setting.Type.TEXT).get().getValue());
        int size = getSetting(Setting.Type.SIZE).get().getValue();
        String fontName = getSetting(Setting.Type.FONT).get().getValue();
        int style = getSetting(Setting.Type.STYLE).get().getValue();

        Font font = new Font(fontName, style, size);
        Color color = getSetting(Setting.Type.COLOR).get().getValue();

        Integer xOffset = getSetting(Setting.Type.X_OFFSET).get().getValue();
        Integer yOffset = getSetting(Setting.Type.Y_OFFSET).get().getValue();

        graphics.drawImage(drawUtil.drawFancyText(image.getWidth(), image.getHeight(), text, font, color, xOffset,
                yOffset), 0, 0, null);

        return image;
    }
}
