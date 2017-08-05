package com.pasha.rabbitutils.processor;

import com.pasha.rabbitutils.command.TeachCommand;

public class TeachCommandProcessor implements IProcessor<TeachCommand> {

    private TeachCommand teachCommand;

    @Override
    public void sendMessage(TeachCommand message) {
        this.teachCommand = message;
    }

    @Override
    public void run() {
        // process teach command
    }
}
