package com.pasha.rabbitutils.reader;

/**
 * A class that reads the commands from the file.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class FileCommandReader implements ICommandReader {

    @Override
    public void run() {
        System.out.println("I will read commands from the file :)");
    }
}
