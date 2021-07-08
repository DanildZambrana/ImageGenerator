package renders;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import utils.DrawUtil;
import utils.FontStyle;
import utils.PlaceholderUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Set;

public class TextRender extends AbstractRender {
    private PlaceholderUtil placeholderUtil;
    private DrawUtil drawUtil;

    public TextRender(Set<Setting> settings, int allowedWidth, int allowedHeight, PlaceholderUtil placeholderUtil,
                      DrawUtil drawUtil) {
        super(settings, allowedWidth, allowedHeight, Logger.getLogger(TextRender.class));
        this.placeholderUtil = placeholderUtil;
        this.drawUtil = drawUtil;

        if (!hasSetting(Setting.Type.TEXT)) {
            getSettings().add(new Setting(Setting.Type.TEXT, "DEFAULT TEXT VALUE"));
        }

        String defaultFont = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0];
        if (!hasSetting(Setting.Type.FONT)) {
            getSettings().add(new Setting(Setting.Type.FONT, defaultFont));
        }

        Setting fontSetting = getSetting(Setting.Type.FONT);
        if (!Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
                .contains(fontSetting.getValue())) {
            getLogger().warn("The specified font cannot be loaded, using default font, '" + defaultFont + "'!");
            getSettings().add(new Setting(Setting.Type.FONT, defaultFont));
        }

        if (!this.hasSetting(Setting.Type.SIZE)) {
            getSettings().add(new Setting(Setting.Type.SIZE, 20));
        }

        Setting sizeSetting = getSetting(Setting.Type.SIZE);
        try {
            int temp = ((Integer) sizeSetting.getValue());
        } catch (NumberFormatException | ClassCastException e) {
            getLogger().warn("The size of the text is invalid. Current value = '" + sizeSetting.getValue() + "' using"
                    + " default size. '" + 20 + "'");
        }

        if (!hasSetting(Setting.Type.COLOR)) {
            getSettings().add(new Setting(Setting.Type.COLOR, new Color(255, 255, 255)));
        }

        Setting colorSetting = getSetting(Setting.Type.COLOR);
        try {
            if (!(colorSetting.getValue() instanceof Color)) {
                String[] value = ((String) colorSetting.getValue()).split(",");
                getSettings().add(new Setting(Setting.Type.COLOR,
                        new Color(Integer.parseInt(value[0]), Integer.parseInt(value[1]), Integer.parseInt(value[2]))));
            }
        } catch (Exception e) {
            getLogger().warn("An error has occurred when trying to load font color, using default value '255, 255, 255'");
        }

        if (!hasSetting(Setting.Type.STYLE)) {
            getSettings().add(new Setting(Setting.Type.STYLE, "PLAIN"));
        }

        Setting styleSetting = getSetting(Setting.Type.STYLE);
        try {
            FontStyle.valueOf(((String) styleSetting.getValue()).toUpperCase());
        } catch (Exception e) {
            getLogger().warn("The provided font style is invalid, using default value 'PLAIN'");
            getSettings().add(new Setting(Setting.Type.STYLE, "PLAIN"));
        }

        if (!hasSetting(Setting.Type.X_OFFSET)) {
            getSettings().add(new Setting(Setting.Type.X_OFFSET, "CENTERED"));
        }

        if (!hasSetting(Setting.Type.Y_OFFSET)) {
            getSettings().add(new Setting(Setting.Type.Y_OFFSET, "CENTERED"));
        }
    }

    /**
     * Draw text in provided image.
     * @param image the image to draw the text.
     * @param graphics graphics instance to draw text.
     * @return null;
     */
    @Override
    @Nullable
    public Image renderImage(BufferedImage image, Graphics2D graphics) {
        String text = placeholderUtil.replacePlaceholders((String) getSetting(Setting.Type.TEXT).getValue());
        int size = (int) getSetting(Setting.Type.SIZE).getValue();

        String fontName = (String) getSetting(Setting.Type.FONT).getValue();

        int style = FontStyle.valueOf(((String) getSetting(Setting.Type.STYLE).getValue()).toUpperCase()).getValue();
        Font font = new Font(fontName, style, size);
        Color color = (Color) getSetting(Setting.Type.COLOR).getValue();

        Integer xOffset = null;

        if (!((String) getSetting(Setting.Type.X_OFFSET).getValue()).equalsIgnoreCase("CENTERED")) {
            try {

                xOffset = (Integer) getSetting(Setting.Type.X_OFFSET).getValue();
            }catch (Exception e) {
                xOffset = null;
            }
        }

        Integer yOffset = null;
        if (((String) getSetting(Setting.Type.Y_OFFSET).getValue()).equalsIgnoreCase("CENTERED")) {
            try {

                yOffset = (Integer) getSetting(Setting.Type.Y_OFFSET).getValue();
            }catch (Exception e) {
                yOffset = null;
            }
        }


        graphics.drawImage(drawUtil.drawFancyText(image.getWidth(), image.getHeight(), text, font, color, xOffset,
                yOffset), 0, 0, null);

        return null;
    }
}
