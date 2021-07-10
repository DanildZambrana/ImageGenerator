package com.github.danildzambrana.imagegenerator.utils;

import java.awt.*;
import java.util.Optional;

public class Converters {
    /**
     * Convert valid string to a color instance
     * valid string is 0,0,0 to 255,255,255
     * @param value the string to convert.
     * @return if the value provided is a valid RGB color return the color, otherwise return empty optional..
     */
    public static Optional<Color> convertStringToColor(String value) {
        String[] values = value.split(",");
        try {
            return Optional.of(new Color(
                    Integer.parseInt(values[0]),
                    Integer.parseInt(values[1]),
                    Integer.parseInt(values[2])
            ));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    /**
     * Convert String to {@link Font} style.
     * @param value the value to convert.
     * @return if the provided value is a style return the font style, otherwise return optional empty.
     */
    public static Optional<Integer> convertStringToFontStyle(String value) {
        try {
            return Optional.of(FontStyle.valueOf(value).getValue());
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}
