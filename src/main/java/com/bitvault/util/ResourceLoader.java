package com.bitvault.util;

import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;

/**
 * Utility class which manages the access to this project's assets.
 * Helps keeping the assets files structure organized.
 */
public class ResourceLoader {

    private ResourceLoader() {
    }

    public static URL loadURL(String path) {
        return ResourceLoader.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toExternalForm();
    }

    public static InputStream loadStream(String name) {
        return ResourceLoader.class.getResourceAsStream(name);
    }

    public static <R> R loadFromStream(String name, Function<InputStream, R> function) {
        try (var stream = loadStream(name)) {
            return function.apply(stream);
        } catch (Exception e) {
            throw new RuntimeException("failed to load:" + name, e);
        }

    }

}
