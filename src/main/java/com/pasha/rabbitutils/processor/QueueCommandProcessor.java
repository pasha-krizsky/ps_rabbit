package com.pasha.rabbitutils.processor;

import com.pasha.rabbitutils.command.QueueCommand;

public class QueueCommandProcessor implements IProcessor<QueueCommand> {

    private QueueCommand queueCommand;

    @Override
    public void sendMessage(QueueCommand message) {
        this.queueCommand = message;
    }

    @Override
    public void run() {
        // process queue command
    }
}
