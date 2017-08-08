package com.pasha.rabbitutils.reader;

import com.pasha.rabbitutils.keeper.CommandsKeeper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that reads the commands.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class CommonCommandReader implements ICommandReader {

    /** Counter of commands. */
    private static int countCommands = 1;

    /** Keeper of commands. */
    private CommandsKeeper commandsKeeper;

    /** ... */
    private static final String HELLO_MESSAGE =
            ".__           .__  .__          \n"    +
            "|  |__   ____ |  | |  |   ____  \n"    +
            "|  |  \\_/ __ \\|  | |  |  /  _ \\ \n" +
            "|   Y  \\  ___/|  |_|  |_(  <_> )\n"   +
            "|___|  /\\___  >____/____/\\____/ \n"  +
            "     \\/     \\/                  ";


    /** Gets command keeper. */
    public CommonCommandReader(CommandsKeeper commandsKeeper) {
        this.commandsKeeper = commandsKeeper;
    }

    /** Runs new thread. */
    @Override
    public void run() {
        sayHello();
        readAnotherCommand();
    }

    /** Reads commands and writes them to commands keeper. */
    private void readAnotherCommand() {

        Scanner in = new Scanner(System.in);

        System.out.println("Check if there are commands in 'commands' file...");
        String relativePath = "/src/main/resources/commands";
        String absolutePath = new File("").getAbsolutePath();

        // Read commands and write them to Commands Keeper
        try (Stream<String> stream = Files.lines(Paths.get(absolutePath + relativePath))) {
            List<String> lines = stream.collect(Collectors.toList());

            for (String line: lines) {
                commandsKeeper.writeCommand(line);
                System.out.println("  [" + countCommands + "] " + "command is processing now...");
                countCommands++;
                Thread.sleep(1000L);
            }

        } catch (Exception e) {
            System.out.println("Cannot read commands from 'commands' file...");
        }

        // Read commands from console
        while (true) {

            // Read command
            String command = in.nextLine();

            // Add command to the keeper
            commandsKeeper.writeCommand(command);

            // ...
            System.out.println("  [" + countCommands + "] " + "command is processing now...");

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted");
                e.printStackTrace();
            }

            countCommands++;
            // And... read another command
        }
    }

    /** ... */
    private void sayHello() {

        System.out.println(HELLO_MESSAGE);
        System.out.println("Welcome to the Publish-Subscribe-RabbitMQ-Console!");
        System.out.println();
    }
}
