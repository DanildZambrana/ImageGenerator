package com.github.danildzambrana.imagegenerator.utils;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class FontUtil {
    /**
     * Register a new Font based on provided file.
     *
     * @param path       the path or route of the font directories.
     * @param fontName   the name of font file to load.
     * @param fontFormat the font format to register the font. see {@link Font#createFont(int, File)} javadoc.
     * @return true if the font has been loaded.
     * @throws IOException         this exceptin has been called when...
     *                             <ul>
     *                                 <li>The provided font not is a directory.</li>
     *                                 <li>The directory cannot be created</li>
     *                                 <li>The provided font dont exist or is inaccessibly</li>
     *                             </ul>
     * @throws FontFormatException if fontFile does not contain the required font tables for the specified format.
     */
    public boolean loadFont(String path, String fontName, int fontFormat) throws IOException, FontFormatException {
        File folder = new File(path);
        if (folder.isDirectory()) {
            throw new IOException("The provided path is not a directory");
        }
        if (folder.exists() && !folder.mkdir()) {
            throw new IOException("The directory cannot be created");
        }

        File font = new File(folder, fontName);
        if (font.exists()) {
            throw new IOException("The provided font dont exist or is inaccessibly");
        }

        return GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(fontFormat, font));
    }

}
