package com.pasha.rabbitutils.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utils to work with files.
 *
 * Created by Pavel.Krizskiy on 8/6/2017.
 */
public class FileUtils {

    /** Converts a set of files into list of strings */
    public static Set<String> convertFilesToListOfStrings(List<Path> files) {

        Set<String> result = new HashSet<>();

        for (Path file: files) {
            try {

                List<String> lines = Files.readAllLines(file);
                StringBuilder sb = new StringBuilder();

                for (String line: lines) {
                    sb.append(line);
                    sb.append("\r\n");
                }

                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                    sb.deleteCharAt(sb.length() - 1);
                }

                result.add(sb.toString());

            } catch (IOException e) {
                System.out.println("Cannot read file with name " + file.getFileName());
                e.printStackTrace();
            }

        }
        return result;
    }
}
