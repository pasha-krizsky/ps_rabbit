package com.pasha.rabbitutils.processor;

import com.pasha.rabbitutils.command.TeachCommand;

/**
 * A class that processes the "teach" command.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class TeachCommandProcessor implements IProcessor<TeachCommand> {

    /** The "teach" command. */
    private TeachCommand teachCommand;

    /** Receives the "teach" command. */
    @Override
    public void sendCommand(TeachCommand command) {
        this.teachCommand = command;
    }

    /** Processes the "teach" command. */
    @Override
    public void run() {
        // process teach command
    }
}
