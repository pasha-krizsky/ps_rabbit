package com.pasha.rabbitutils.processor;

import com.pasha.rabbitutils.command.ExchangeCommand;

public class ExchangeCommandProcessor implements IProcessor<ExchangeCommand> {

    private ExchangeCommand exchangeCommand;

    @Override
    public void sendMessage(ExchangeCommand message) {
        this.exchangeCommand = message;
    }

    @Override
    public void run() {
        // process exchange command
    }
}
