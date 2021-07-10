package com.github.danildzambrana.imagegenerator.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Based on
 * <a href="https://github.com/Andre601/BannerBoard-free/blob/main/BannerBoard_plugin/src/me/bigteddy98/bannerboard/util/DrawUtil.java">DrawUtil</a>
 */
public class DrawUtil {
    /**
     * Generate a buffered image with text.
     * @param width The width of the image base.
     * @param height The height of the image base.
     * @param text The text to draw.
     * @param font the font used to generate the text.
     * @param textColor The color of the text.
     * @param xOffset Position of the text in base image, if is -1 Centered is used by default.
     * @param yOffset Position of the text in base image, if is -1 Centered is used by default.
     * @return the buffered image with the text.
     */
    public BufferedImage drawFancyText(int width, int height, String text, Font font, Color textColor, Integer xOffset,
                                       Integer yOffset) {
        BufferedImage textLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D textGraphics = textLayer.createGraphics();

        try {
            textGraphics.setColor(textColor);
            textGraphics.setFont(font);

            FontMetrics fontMetrics = textGraphics.getFontMetrics();
            if (xOffset == -1) { //Set to center
                xOffset = (width - fontMetrics.stringWidth(text)) / 2;
            }

            if (yOffset == -1) { //Set to center
                yOffset = (fontMetrics.getAscent() + (height - (fontMetrics.getAscent() + fontMetrics.getDescent())) / 2);
            }

            textGraphics.drawString(text, xOffset, yOffset);
            return textLayer;
        } finally {
            textGraphics.dispose();
        }
    }
}
