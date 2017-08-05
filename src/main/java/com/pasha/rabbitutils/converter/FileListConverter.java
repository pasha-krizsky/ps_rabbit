package com.pasha.rabbitutils.converter;

import com.beust.jcommander.IStringConverter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that converts list of string arguments into a list of files.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class FileListConverter implements IStringConverter<List<Path>> {

    /**
     * A method that converts the list of paths to files in string representation into the list of
     * java.nio.file.Path objects.
     * @param files - String with paths to files splitted by comma.
     * @return - List of java.nio.file.Path objects.
     */
    public List<Path> convert(String files) {

        // Create array of paths to files
        String [] pathsToFiles = files.split(",");
        List<Path> fileList = new ArrayList<>();

        // Create list of files
        for(String pathToFile: pathsToFiles){
            Path path = Paths.get(pathToFile);
            fileList.add(path);
        }

        return fileList;
    }
}
