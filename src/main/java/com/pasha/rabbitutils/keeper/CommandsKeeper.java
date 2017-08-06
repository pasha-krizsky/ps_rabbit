package com.pasha.rabbitutils.keeper;

import java.util.LinkedList;
import java.util.List;

/**
 * A class that keeps all the commands.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class CommandsKeeper {

    /** List of commands */
    private List<String> commands;

    /** Max number of commands in queue */
    private static final int MAX_AMOUNT_OF_COMMANDS = 31;

    public CommandsKeeper() {
        commands = new LinkedList<>();
    }

    /** Read first command from the queue */
    public synchronized String readCommand() {

        // If there are no commands, sleep...
        if (commands.size() < 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted");
                e.printStackTrace();
            }
        }

        // There is a command! Read and remove.
        String command = commands.get(0);
        commands.remove(0);
        notify();
        return command;
    }

    /** Write new command to the queue */
    public synchronized void writeCommand(String message) {

        // To many commands in queue, sleep...
        if (commands.size() >= MAX_AMOUNT_OF_COMMANDS) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted");
                e.printStackTrace();
            }
        }

        // Add new command to the queue
        commands.add(message);
        notify();
    }
}
