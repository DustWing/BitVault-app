package com.bitvault.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T read(InputStream inputStream, Class<T> tClass) {

        try {
            return Json.MAPPER.readValue(inputStream, tClass);
        } catch (IOException e) {
            return null;
        }
    }
}
