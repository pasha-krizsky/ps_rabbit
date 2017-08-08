package com.pasha.rabbitutils;

import com.pasha.rabbitutils.keeper.CommandsKeeper;
import com.pasha.rabbitutils.processor.CommandsSpreader;
import com.pasha.rabbitutils.reader.CommonCommandReader;

/**
 * The main class.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class App {

    /** Entry point */
    public static void main(String[] args) throws Exception {

        // Common commands keeper
        CommandsKeeper commandsKeeper = new CommandsKeeper();

        // Console reader
        CommonCommandReader commonCommandReader = new CommonCommandReader(commandsKeeper);
        // And commands spreader
        CommandsSpreader commandsSpreader = new CommandsSpreader(commandsKeeper);

        // Create two threads, one for console reader, one for commands spreader
        Thread threadReader = new Thread(commonCommandReader);
        Thread threadProcessor = new Thread(commandsSpreader);

        // Run threads
        threadReader.start();
        threadProcessor.start();

        // Wait for threads
        threadReader.join();
        threadProcessor.join();
    }
}
