package com.github.danildzambrana.imagegenerator.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class DownloadUtils {
    /**
     * Open stream from url.
     * @param url the link to get the data.
     * @return instance of InputStream.
     * @throws IOException if an I/O exception occurs.
     */
    public static InputStream openStream(String url) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setConnectTimeout(2000);
        urlConnection.setReadTimeout(5000);

        return urlConnection.getInputStream();
    }

    /**
     * Download file to provided path with custom executor.
     *
     * @param executor the executor to use.
     * @param url      the url to download the file.
     * @param path     the path to download the file.
     * @param fileName the name of download file.
     * @return instance of {@link CompletableFuture} with download file process.
     */
    public static CompletableFuture<File> downloadFileAsync(Executor executor,
                                                            String url,
                                                            String path,
                                                            String fileName) {
        Supplier<File> supplier = () -> {
            try {
                File folder = new File(path);

                if (folder.exists() && !folder.isDirectory()) {
                    throw new IOException("The provided path is not a directory");
                }

                if (!folder.exists() && !folder.mkdir()) {
                    throw new IOException("The directory cannot be created");
                }

                InputStream inputStream;
                OutputStream outputStream;
                File file = new File(folder, fileName);

                inputStream = openStream(url);
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

    /**
     * Download file to provided path.
     *
     * @param url      the url to download the file.
     * @param path     the path to download the file.
     * @param fileName the name of download file.
     * @return instance of {@link CompletableFuture} with download file process.
     */
    public static CompletableFuture<File> downloadFileAsync(String url, String path, String fileName) {
        return downloadFileAsync(null, url, path, fileName);
    }
}
