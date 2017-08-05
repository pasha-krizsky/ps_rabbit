package com.pasha.rabbitutils.converter;

import com.beust.jcommander.IStringConverter;

import java.util.Arrays;
import java.util.List;

/**
 * A class that converts string with arguments into a list of strings.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class StringListConverter implements IStringConverter<List<String>> {

    /**
     * A method that converts a string into a list of strings.
     */
    public List<String> convert(String strings) {
        return Arrays.asList(strings.split(","));
    }
}
