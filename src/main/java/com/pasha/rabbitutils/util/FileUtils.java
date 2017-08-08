package com.pasha.rabbitutils.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utils to work with files.
 *
 * Created by Pavel.Krizskiy on 8/6/2017.
 */
public class FileUtils {

    /** Converts a list of files into set of strings */
    public static Set<String> convertFilesToListOfStrings(List<Path> files) {

        // Set of contents of files
        Set<String> result = new HashSet<>();

        for (Path file: files) {
            try {
                List<String> lines = Files.readAllLines(file);
                StringBuilder sb = new StringBuilder();

                // Add file content to StringBuilder
                for (String line: lines) {
                    sb.append(line);
                    sb.append("\r\n");
                }

                // Remove last symbols
                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                    sb.deleteCharAt(sb.length() - 1);
                }

                // Add file's content
                result.add(sb.toString());

            } catch (IOException e) {
                System.out.println("Cannot read file with name " + file.getFileName());
                e.printStackTrace();
            }
        }

        return result;
    }

    /** Reads URI from file. */
    public static String readURIFromFile() {

        String relativePath = "/src/main/resources/app.conf";
        String absolutePath = new File("").getAbsolutePath();
        Path uriPath = Paths.get(absolutePath + relativePath);

        List<String> uri = null;

        try {
            uri = Files.readAllLines(uriPath);
        } catch (IOException e) {
            System.out.println("Cannot read URI from file: " + absolutePath + relativePath);
            e.printStackTrace();
        }

        return uri.get(0);
    }
}
