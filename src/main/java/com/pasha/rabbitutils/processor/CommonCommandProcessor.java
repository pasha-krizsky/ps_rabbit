package com.pasha.rabbitutils.processor;

import com.beust.jcommander.JCommander;
import com.pasha.rabbitutils.command.ExchangeCommand;
import com.pasha.rabbitutils.command.TeachCommand;
import com.pasha.rabbitutils.command.QueueCommand;

/**
 * A class that processes the commands.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class CommonCommandProcessor implements IProcessor<String> {

    private String message;

    private QueueCommand queueCommand;
    private TeachCommand teachCommand;
    private ExchangeCommand exchangeCommand;

    private QueueCommandProcessor queueCommandProcessor;
    private ExchangeCommandProcessor exchangeCommandProcessor;
    private TeachCommandProcessor teachCommandProcessor;

    private JCommander jc;

    public CommonCommandProcessor() {

        this.queueCommand = new QueueCommand();
        this.teachCommand = new TeachCommand();
        this.exchangeCommand = new ExchangeCommand();

        this.queueCommandProcessor = new QueueCommandProcessor();
        this.exchangeCommandProcessor = new ExchangeCommandProcessor();
        this.teachCommandProcessor = new TeachCommandProcessor();

        jc = new JCommander();
        jc.addCommand("queue", queueCommand);
        jc.addCommand("teach", teachCommand);
        jc.addCommand("exchange", exchangeCommand);
    }

    public void sendMessage(String message) {
        this.message = message;
    }

    @Override
    public void run() {

        jc.parse(message.split(" "));

        switch (jc.getParsedCommand()) {
            case "queue":
                queueCommandProcessor.sendMessage(queueCommand);
                new Thread(queueCommandProcessor).start();
                break;
            case "teach":
                teachCommandProcessor.sendMessage(teachCommand);
                new Thread(teachCommandProcessor).start();
                break;
            case "exchange":
                exchangeCommandProcessor.sendMessage(exchangeCommand);
                new Thread(exchangeCommandProcessor).start();
                break;
        }
    }
}
