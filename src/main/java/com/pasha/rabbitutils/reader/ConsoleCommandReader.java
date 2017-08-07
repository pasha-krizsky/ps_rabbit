package com.pasha.rabbitutils.reader;

import com.pasha.rabbitutils.keeper.CommandsKeeper;

import java.util.Scanner;

/**
 * A class that reads the commands from console.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class ConsoleCommandReader implements ICommandReader {

    /** Counter of commands. */
    private static int countCommands = 1;

    /** Keeper of commands. */
    private CommandsKeeper commandsKeeper;

    private static final String HELLO_MESSAGE =
            ".__           .__  .__          \n"    +
            "|  |__   ____ |  | |  |   ____  \n"    +
            "|  |  \\_/ __ \\|  | |  |  /  _ \\ \n" +
            "|   Y  \\  ___/|  |_|  |_(  <_> )\n"   +
            "|___|  /\\___  >____/____/\\____/ \n"  +
            "     \\/     \\/                  ";

    /** Gets command keeper. */
    public ConsoleCommandReader(CommandsKeeper commandsKeeper) {
        this.commandsKeeper = commandsKeeper;
    }

    @Override
    public void run() {
        sayHello();
        readAnotherCommand();
    }

    /** Reads commands from console and writes them to commands keeper. */
    private void readAnotherCommand() {

        Scanner in = new Scanner(System.in);

        // Read commands until...
        while (true) {

            String command = in.nextLine();

            // Add command to the keeper
            commandsKeeper.writeCommand(command);

            // ...
            System.out.println("  [" + countCommands + "] " + "command is processing now...");
            countCommands++;
            // And... read another command
        }
    }

    /** ... */
    private void sayHello() {

        System.out.println(HELLO_MESSAGE);
        System.out.println("Welcome to the Publish Subscribe RabbitMQ Console!");
        System.out.println();
    }
}
