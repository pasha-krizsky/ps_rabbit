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

    public CommandsSpreader(CommandsKeeper commandsKeeper) throws Exception {

        this.dataKeeper = new DataKeeper();
        this.commandsKeeper = commandsKeeper;
    }

    @Override
    public void run() {
        readAndSpread();
    }

    /** Reads another command and give it to the concrete command processor */
    private void readAndSpread() {

        // Read it again and again
        while (true) {

            QueueCommand queueCommand = new QueueCommand();
            TeachCommand teachCommand = new TeachCommand();
            ExchangeCommand exchangeCommand = new ExchangeCommand();

            JCommander jc = new JCommander();

            jc.addCommand("queue", queueCommand);
            jc.addCommand("teach", teachCommand);
            jc.addCommand("exchange", exchangeCommand);

            // Read
            String command = commandsKeeper.readCommand();

            try {
                // Parse
                jc.parse(command.split(" "));
            } catch (Exception e) {
                System.out.println("Cannot parse command, try again...");
                continue;
            }

            System.out.println("Command was successfully parsed!");

            // Spread
            switch (jc.getParsedCommand()) {

                case "queue":

                    jc = new JCommander();
                    jc.addCommand("queue", queueCommand);
                    QueueCommandProcessor queueCommandProcessor = new QueueCommandProcessor();
                    queueCommandProcessor.sendCommand(queueCommand);
                    queueCommandProcessor.process();

                    break;

                case "teach":

                    jc = new JCommander();
                    jc.addCommand("teach", teachCommand);
                    TeachCommandProcessor teachCommandProcessor = new TeachCommandProcessor();
                    teachCommandProcessor.sendCommand(teachCommand);
                    teachCommandProcessor.setDataKeeper(dataKeeper);
                    teachCommandProcessor.process();

                    break;

                case "exchange":

                    jc = new JCommander();
                    jc.addCommand("exchange", exchangeCommand);
                    ExchangeCommandProcessor exchangeCommandProcessor = new ExchangeCommandProcessor();
                    exchangeCommandProcessor.sendCommand(exchangeCommand);
                    exchangeCommandProcessor.process();

                    break;
            }

            System.out.println();
        }
    }
}
