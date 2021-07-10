package renders;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import utils.Converters;
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
        if (optionalColorSetting.isEmpty()) {
            getLogger().warn("Render color of font is empty. Using default Value = 0, 0, 0");
            addSetting(Setting.Type.COLOR, new Color(0, 0, 0, 255));
        } else {
            Setting setting = optionalColorSetting.get();

            if (!(setting.getValue() instanceof Color) && setting.getValue() instanceof String) {
                Optional<Color> result = Converters.convertStringToColor(setting.getValue());
                if (result.isEmpty()) {
                    addSetting(Setting.Type.COLOR, new Color(255, 255, 255, 255));
                    getLogger().warn("This value= '" + setting.getValue() + "' not is a color or not is convertible "
                            + "to color!. \n Using default value = 255,255,255");
                } else {
                    addSetting(Setting.Type.COLOR, result.get());
                }
            }
        }
        //Load Style
        Optional<Setting> optionalStyleSetting = getSetting(Setting.Type.STYLE);
        if (optionalStyleSetting.isEmpty()) {
            getLogger().warn("Render style of font is empty. Using default value = PLAIN");
            addSetting(Setting.Type.STYLE, Font.PLAIN);
        } else {
            Setting setting = optionalStyleSetting.get();
            if (!(setting.getValue() instanceof Integer) && setting.getValue() instanceof String) {
                Optional<Integer> result = Converters.convertStringToFontStyle(setting.getValue());

                if (result.isEmpty()) {
                    addSetting(Setting.Type.STYLE, Font.PLAIN);
                    getLogger().warn("Provided Style value is not supported. Using default value = PLAIN");
                } else {
                    addSetting(Setting.Type.STYLE, result.get());
                }
            }
        }

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
