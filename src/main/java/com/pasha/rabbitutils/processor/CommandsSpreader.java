package com.pasha.rabbitutils.processor;

import com.beust.jcommander.JCommander;
import com.pasha.rabbitutils.command.ExchangeCommand;
import com.pasha.rabbitutils.command.TeachCommand;
import com.pasha.rabbitutils.command.QueueCommand;
import com.pasha.rabbitutils.keeper.CommandsKeeper;
import com.pasha.rabbitutils.keeper.DataKeeper;

/**
 * A class that spreads the commands.
 *
 * Created by Pavel.Krizskiy on 8/6/2017.
 */
public class CommandsSpreader implements Runnable {

    /** Keeper to read commands. */
    private CommandsKeeper commandsKeeper;

    /** Data Keeper :) */
    private DataKeeper dataKeeper;

    // All commands
    private QueueCommand queueCommand;
    private TeachCommand teachCommand;
    private ExchangeCommand exchangeCommand;

    // All command processors
    private QueueCommandProcessor queueCommandProcessor;
    private ExchangeCommandProcessor exchangeCommandProcessor;
    private TeachCommandProcessor teachCommandProcessor;

    /** JCommander */
    private JCommander jc;

    public CommandsSpreader(CommandsKeeper commandsKeeper) throws Exception {

        this.dataKeeper = new DataKeeper();
        this.commandsKeeper = commandsKeeper;

        this.queueCommandProcessor = new QueueCommandProcessor();
        this.exchangeCommandProcessor = new ExchangeCommandProcessor();
        this.teachCommandProcessor = new TeachCommandProcessor();
    }

    @Override
    public void run() {
        readAndSpread();
    }

    /** Reads another command and give it to the concrete command processor */
    private void readAndSpread() {

        // Read it again and again
        while (true) {

            this.queueCommand = new QueueCommand();
            this.teachCommand = new TeachCommand();
            this.exchangeCommand = new ExchangeCommand();

            jc = new JCommander();
            jc.addCommand("queue", queueCommand);
            jc.addCommand("teach", teachCommand);
            jc.addCommand("exchange", exchangeCommand);

            // Read
            String command = commandsKeeper.readCommand();

            try {
                // Parse
                jc.parse(command.split(" "));
            } catch (Exception e) {
                System.out.println("Bad command, try again...");
                continue;
            }

            System.out.println("Command was successfully parsed!");

            // Spread
            switch (jc.getParsedCommand()) {

                case "queue":
                    jc = new JCommander();
                    jc.addCommand("queue", queueCommand);
                    queueCommandProcessor.sendCommand(queueCommand);

                    Thread queueCommandProcessorThread = new Thread(queueCommandProcessor);
                    queueCommandProcessorThread.start();

                    try {
                        queueCommandProcessorThread.join();
                    } catch (InterruptedException e) {
                        System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted");
                        e.printStackTrace();
                    }

                    break;

                case "teach":
                    jc = new JCommander();

                    jc.addCommand("teach", teachCommand);
                    teachCommandProcessor = new TeachCommandProcessor();
                    teachCommandProcessor.sendCommand(teachCommand);
                    teachCommandProcessor.setDataKeeper(dataKeeper);

                    Thread teachCommandProcessorThread = new Thread(teachCommandProcessor);
                    teachCommandProcessorThread.start();

                    try {
                        teachCommandProcessorThread.join();
                    } catch (InterruptedException e) {
                        System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted");
                        e.printStackTrace();
                    }

                    break;

                case "exchange":
                    jc = new JCommander();
                    jc.addCommand("exchange", exchangeCommand);
                    exchangeCommandProcessor.sendCommand(exchangeCommand);

                    Thread exchangeCommandProcessorThread = new Thread(exchangeCommandProcessor);
                    exchangeCommandProcessorThread.start();

                    try {
                        exchangeCommandProcessorThread.join();
                    } catch (InterruptedException e) {
                        System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted");
                        e.printStackTrace();
                    }

                    break;
            }

            System.out.println();
        }
    }
}
