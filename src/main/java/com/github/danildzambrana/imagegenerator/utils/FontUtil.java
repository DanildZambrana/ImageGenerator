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

    /**
     * Download file to provided path.
     *
     * @param executor the executor to use.
     * @param url      the url to download the file.
     * @param path     the path to download the file.
     * @param fileName the name of download file.
     * @return instance of {@link CompletableFuture} with download file process.
     */
    public CompletableFuture<File> downloadFileAsync(Executor executor, String url, String path, String fileName) {
        Supplier<File> supplier = () -> {
            try {
                File folder = new File(path);

                if (!folder.isDirectory()) {
                    throw new IOException("The provided path is not a directory");
                }

                if (folder.exists() && !folder.mkdir()) {
                    try {
                        throw new IOException("The directory cannot be created");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                InputStream inputStream;
                OutputStream outputStream;
                File file = new File(folder, fileName);

                URLConnection connection = new URL(url).openConnection();
                connection.connect();

                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(file);

                int b = 0; //b is byte
                while (b != -1) {
                    b = inputStream.read();
                    if (b != -1) {
                        outputStream.write(b);
                    }
                }

                outputStream.close();
                inputStream.close();

                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        };

        if (executor == null) {
            return CompletableFuture.supplyAsync(supplier);
        } else {
            return CompletableFuture.supplyAsync(supplier, executor);
        }
    }

    public CompletableFuture<File> downloadFileAsync(String url, String path, String fileName) {
        return downloadFileAsync(null, url, path, fileName);
    }
}
