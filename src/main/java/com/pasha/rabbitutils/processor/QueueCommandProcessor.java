package com.pasha.rabbitutils.processor;

import com.pasha.rabbitutils.bus.RabbitMQWrapper;
import com.pasha.rabbitutils.command.QueueCommand;
import com.pasha.rabbitutils.util.FileUtils;

import java.io.IOException;

/**
 * A class that processes the "queue" command.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class QueueCommandProcessor implements IProcessor<QueueCommand> {

    /** The "queue" command. */
    private QueueCommand queueCommand;

    /** RabbitMQ Wrapper. */
    private RabbitMQWrapper rabbitMQWrapper;

    /** Initialize RabbitMQ Wrapper. */
    public QueueCommandProcessor() {

        String uri = FileUtils.readURIFromFile();
        rabbitMQWrapper = new RabbitMQWrapper();

        try {
            rabbitMQWrapper.createConnection(uri);
            rabbitMQWrapper.createChannel();
        } catch (Exception e) {
            System.out.println("Cannot open connection or create channel");
            e.printStackTrace();
        }
    }

    /** Receives the "queue" command. */
    @Override
    public void sendCommand(QueueCommand command) {
        this.queueCommand = command;
    }

    /** Processes the "queue" command. */
    @Override
    public void run() {
        if (queueCommand.getQueueNamesToWrite() != null) {
            if (queueCommand.getMessageToSend() != null) {
                sendMessageToQueue();
            }
        }
        // process queue command
    }

    /** Sends all messages to all queues */
    private void sendMessageToQueue() {

        for (String queue: queueCommand.getQueueNamesToWrite()) {
            for (String msg: queueCommand.getMessageToSend()) {
                try {
                    rabbitMQWrapper.publishMessage(queue, msg);
                } catch (IOException ignored) {}
            }
        }
    }
}
