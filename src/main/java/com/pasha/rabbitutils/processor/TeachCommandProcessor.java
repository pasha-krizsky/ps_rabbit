package com.pasha.rabbitutils.processor;

import com.pasha.rabbitutils.bus.RabbitMQWrapper;
import com.pasha.rabbitutils.command.TeachCommand;
import com.pasha.rabbitutils.keeper.DataKeeper;
import com.pasha.rabbitutils.util.FileUtils;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that processes the "teach" command.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class TeachCommandProcessor implements IProcessor<TeachCommand> {

    /** The "teach" command. */
    private TeachCommand teachCommand;

    /** Data Keeper :) */
    @Setter
    private DataKeeper dataKeeper;

    private RabbitMQWrapper rabbitMQWrapper;

    public TeachCommandProcessor()
            throws Exception {

        rabbitMQWrapper = new RabbitMQWrapper();
        rabbitMQWrapper.createConnection("amqp://guest:guest@localhost:5672/pasha");
        rabbitMQWrapper.createChannel();
    }

    /** Receives the "teach" command. */
    @Override
    public void sendCommand(TeachCommand command) {
        this.teachCommand = command;
    }

    /** Processes the "teach" command. */
    @Override
    public void run() {

        // process teach command
        if (teachCommand.getQueueNamesToRequests() != null
                && teachCommand.getQueueNamesToResponses() != null
                && teachCommand.getFileNamesWithRequests() != null
                && teachCommand.getQueueNamesToResponses() != null) {

            teachAnswerToRequests();
        }
    }

    /** Teaches the program to reply with concrete answers to concrete requests */
    private void teachAnswerToRequests() {

        Set<String> requestsBodies;
        Set<String> responsesBodies;

        // Lists of file bodies
        requestsBodies = FileUtils.convertFilesToListOfStrings(teachCommand.getFileNamesWithRequests());
        responsesBodies = FileUtils.convertFilesToListOfStrings(teachCommand.getFileNamesWithResponses());

        // Remember requests bodies for queues
        for (String requestQueue: teachCommand.getQueueNamesToRequests()) {
            dataKeeper.getQueueAndRequests().put(requestQueue, requestsBodies);
        }

        // Remember response bodies for queues
        for (String responseQueue: teachCommand.getQueueNamesToResponses()) {
            dataKeeper.getQueueAndResponses().put(responseQueue, responsesBodies);
        }

        for (String requestQueue: teachCommand.getQueueNamesToRequests()) {
            // New set for concrete request queue
            dataKeeper.getRequestAndResponseQueues().put(requestQueue, new HashSet<>());
            // Add queues to response
            for (String responseQueue: teachCommand.getQueueNamesToResponses()) {
                dataKeeper.getRequestAndResponseQueues().get(requestQueue).add(responseQueue);
            }
        }

        // Add listeners for each request queue
        for (String requestQueue: teachCommand.getQueueNamesToRequests()) {
            rabbitMQWrapper.startListenQueueAndSendResponses(requestQueue, dataKeeper);
        }
    }
}
