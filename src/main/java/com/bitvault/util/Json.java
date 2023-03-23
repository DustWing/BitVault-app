package com.bitvault.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> Result<T> deserialize(String value, Class<T> tClass) {

        try {
            T t = MAPPER.readValue(value, tClass);
            return Result.ok(t);
        } catch (IOException e) {
            return Result.error(e);
        }
    }

    public static <T> Result<String> serialize(T value) {

        try {
            String s = MAPPER.writeValueAsString(value);
            return Result.ok(s);
        } catch (IOException e) {
            return Result.error(e);
        }
    }
}
