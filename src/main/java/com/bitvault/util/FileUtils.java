package com.bitvault.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String newLine = System.getProperty("line.separator");

    /**
     * @param uri the path of the file
     * @return A collection of lines which are the content of the file. Order of lines should stay the same
     */
    public static Result<List<String>> readFileToList(final String uri) {

        final Path path = Path.of(uri);
        boolean fileExists = Files.exists(path);

        if (!fileExists) {
            return Result.error(new NoSuchFileException(uri));
        }

        try (final BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            final List<String> resultSet = new ArrayList<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                resultSet.add(line);
            }

            return Result.ok(resultSet);

        } catch (IOException ex) {
            return Result.error(ex);
        }

    }

    /**
     * @param uri the path of the file
     * @return file content as String
     */
    public static Result<String> readFileToString(final String uri) {

        final Path path = Path.of(uri);
        boolean fileExists = Files.exists(path);

        if (!fileExists) {
            return Result.error(new NoSuchFileException(uri));
        }

        try (final BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            final StringBuilder builder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append(newLine);
            }

            return Result.ok(builder.toString());

        } catch (IOException ex) {
            return Result.error(ex);
        }
    }

    /**
     * Creates the file and the Directories needed for the file.
     * if directories exist it will not create or throw exception
     *
     * @param uri     the path of the file
     * @param content file content
     * @return Result of Boolean - true is file created. Result Exception if failed
     */
    public static Result<Boolean> createAndWriteToFile(final String uri, final String content) {
        final Path path = Path.of(uri);

        try {
            Files.createDirectories(path.getParent());
        } catch (IOException ex) {
            return Result.error(ex);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(content, 0, content.length());
            return Result.Success;
        } catch (IOException ex) {
            return Result.error(ex);
        }
    }


}
