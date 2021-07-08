package renders;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import utils.DrawUtil;
import utils.FontStyle;
import utils.PlaceholderUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class TextRender extends AbstractRender {
    private final PlaceholderUtil placeholderUtil;
    private final DrawUtil drawUtil;

    public TextRender(Set<Setting> settings, int allowedWidth, int allowedHeight, PlaceholderUtil placeholderUtil,
                      DrawUtil drawUtil) {
        super(settings, allowedWidth, allowedHeight, Logger.getLogger(TextRender.class));

        this.placeholderUtil = placeholderUtil;
        this.drawUtil = drawUtil;

        //Load Text
        if (!hasSetting(Setting.Type.TEXT)) {
            getSettings().add(new Setting(Setting.Type.TEXT, "DEFAULT TEXT VALUE"));
        }

        //Load Font
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

        //Load Size
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

        //Load font color
        Optional<Setting> optionalColorSetting = getSetting(Setting.Type.COLOR);
        Color color = new Color(255, 255, 255);
        if (optionalColorSetting.isPresent()) {
            Setting setting = optionalColorSetting.get();
            if (!(setting.getValue() instanceof Color)) {
                if (setting.getValue() instanceof String) {
                    String value = setting.getValue();
                    String[] values = value.split(",");
                    try {
                        color = new Color(
                                Integer.parseInt(values[0]),
                                Integer.parseInt(values[1]),
                                Integer.parseInt(values[2])
                        );
                    } catch (NumberFormatException exception) {
                        getLogger().warn("This value= '" + setting.getValue() + "' not is a color or not is convertible "
                                + "to color!. \n Using default value = 255,255,255");
                    }
                } else {
                    getLogger().warn("Provided color value is not supported. Using default value = 255,255,255");
                }
            } else {
                color = setting.getValue();
            }
        }
        addSetting(Setting.Type.COLOR, color);

        //Load Style
        Optional<Setting> optionalStyleSetting = getSetting(Setting.Type.STYLE);
        int style = Font.PLAIN;
        if (optionalStyleSetting.isPresent()) {
            Setting setting = optionalStyleSetting.get();
            if (!(setting.getValue() instanceof Integer)) {
                if (setting.getValue() instanceof String) {
                    try {
                        style = FontStyle.valueOf(setting.getValue()).getValue();
                    } catch (IllegalArgumentException exception) {
                        getLogger().warn("Provided Style value is not supported. Using default value = PLAIN");
                    }
                }else {
                    getLogger().warn("Provided Style value is not supported. Using default value = PLAIN");
                }
            }
        }
        addSetting(Setting.Type.STYLE, style);


        //Load X Offset
        Optional<Setting> optionalXOffsetSetting = getSetting(Setting.Type.X_OFFSET);
        int positionX = -1;
        if (optionalXOffsetSetting.isPresent()) {
            Setting setting = optionalXOffsetSetting.get();
            if (setting.getValue() instanceof Integer) {
                positionX = setting.getValue();
            }else if (setting.getValue() instanceof String) {
                String value = setting.getValue();
                if (!value.equalsIgnoreCase("CENTERED")) {
                    getLogger().warn("Provided value is not supported, using default value CENTERED");
                }
            } else {
                getLogger().warn("Provided value is not supported, using default value CENTERED");
            }
        }
        addSetting(Setting.Type.X_OFFSET, positionX);

        //Load Y Offset
        Optional<Setting> optionalYOffsetSetting = getSetting(Setting.Type.Y_OFFSET);
        int positionY = -1;
        if (optionalYOffsetSetting.isPresent()) {
            Setting setting = optionalYOffsetSetting.get();
            if (setting.getValue() instanceof Integer) {
                positionY = setting.getValue();
            }else if (setting.getValue() instanceof String) {
                String value = setting.getValue();
                if (!value.equalsIgnoreCase("CENTERED")) {
                    getLogger().warn("Provided value is not supported, using default value CENTERED");
                }
            } else {
                getLogger().warn("Provided value is not supported, using default value CENTERED");
            }
        }
        addSetting(Setting.Type.Y_OFFSET, positionY);
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
