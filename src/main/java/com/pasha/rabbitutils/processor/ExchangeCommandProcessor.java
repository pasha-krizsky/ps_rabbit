package com.pasha.rabbitutils.processor;

import com.pasha.rabbitutils.command.ExchangeCommand;

/**
 * A class that processes the "exchange" command.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class ExchangeCommandProcessor implements IProcessor<ExchangeCommand> {

    /** The "exchange" command. */
    private ExchangeCommand exchangeCommand;

    /** Receives the "exchange" command. */
    @Override
    public void sendCommand(ExchangeCommand command) {
        this.exchangeCommand = command;
    }

    /** Processes the "exchange" command. */
    @Override
    public void run() {
        // process exchange command
    }
}
