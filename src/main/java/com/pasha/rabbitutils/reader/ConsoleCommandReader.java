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
    private static int coutCommands = 1;

    /** Keeper of commands. */
    private CommandsKeeper commandsKeeper;

    public ConsoleCommandReader(CommandsKeeper commandsKeeper) {
        this.commandsKeeper = commandsKeeper;
    }

    @Override
    public void run() {
        sayHello();
        readAnotherCommand();
    }

    private void readAnotherCommand() {

        // Read commands until...
        while (true) {

            System.out.print("Command [" + coutCommands + "]" + " >>> ");
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();

            // Add command to the keeper
            commandsKeeper.writeCommand(command);

            // ...
            System.out.println("  [" + coutCommands + "] " + "command is processing now...");
            coutCommands++;
            // And... read another command
        }
    }

    private void sayHello() {

        System.out.println("Welcome to the RabbitMQ Utils Console!");
        System.out.println();
    }
}
