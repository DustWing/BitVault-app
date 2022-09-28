package com.bitvault.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileUtils {


    public static Set<String> readFile(final String path) {

        try (
                final FileReader fileReader = new FileReader(path);
                final BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {

            final Set<String> resultSet = new HashSet<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                resultSet.add(line);
            }

            return resultSet;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }

    }

}
