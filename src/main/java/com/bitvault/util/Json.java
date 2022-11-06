package com.bitvault.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T deserialize(String value, Class<T> tClass) {

        try {
            return Json.MAPPER.readValue(value, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String serialize(T value) {

        try {
            return Json.MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
