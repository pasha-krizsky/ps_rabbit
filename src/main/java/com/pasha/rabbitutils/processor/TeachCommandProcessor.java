package com.pasha.rabbitutils.processor;

import com.pasha.rabbitutils.bus.RabbitMQWrapper;
import com.pasha.rabbitutils.command.TeachCommand;
import com.pasha.rabbitutils.keeper.DataKeeper;
import com.pasha.rabbitutils.util.FileUtils;
import lombok.Setter;
import org.json.JSONObject;

import java.util.*;

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

    public TeachCommandProcessor() {

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

    /** Teaches the program to reply with concrete answers to concrete requests. */
    private void teachAnswerToRequests() {

        Set<String> requestsBodies;
        Set<String> responsesBodies;

        // Lists of file bodies
        requestsBodies = FileUtils.convertFilesToListOfStrings(teachCommand.getFileNamesWithRequests());
        System.out.println("Files with requests were successfully read!");
        responsesBodies = FileUtils.convertFilesToListOfStrings(teachCommand.getFileNamesWithResponses());
        System.out.println("Files with responses were successfully read!");

        List<String> jsonKeys = null;
        // Parse JSON
        if (teachCommand.getNamesOfJSONObjToCompare() != null) {

            System.out.println("Parsing JSON...");

            Set<String> newRequestsBodies = new HashSet<>();

            jsonKeys = Arrays.asList(teachCommand.getNamesOfJSONObjToCompare().get(0).split("\\."));

            // For all requests
            for (String requestBody: requestsBodies) {

                JSONObject json = new JSONObject(requestBody);
                json = (JSONObject) json.get(jsonKeys.get(0));

                for (int i = 1; i < jsonKeys.size(); ++i) {
                    json = (JSONObject) json.get(jsonKeys.get(i));
                }

                newRequestsBodies.add(json.toString());
            }

            requestsBodies = new HashSet<>(newRequestsBodies);
        }

        // Remember requests bodies for queues
        for (String requestQueue: teachCommand.getQueueNamesToRequests()) {
            if (dataKeeper.getQueueAndRequests().get(requestQueue) == null) {
                dataKeeper.getQueueAndRequests().put(requestQueue, new ArrayList<>());
            }

            for (String requestsBody: requestsBodies) {
                dataKeeper.getQueueAndRequests().get(requestQueue).add(requestsBody);
            }
        }

        for (String responseQueue: teachCommand.getQueueNamesToResponses()) {
            if (dataKeeper.getQueueAndResponses().get(responseQueue) == null) {
                dataKeeper.getQueueAndResponses().put(responseQueue, new ArrayList<>());
            }

            for (String responseBody: responsesBodies) {
                dataKeeper.getQueueAndResponses().get(responseQueue).add(responseBody);
            }
        }

        for (String requestQueue: teachCommand.getQueueNamesToRequests()) {
            if (dataKeeper.getRequestAndResponseQueues().get(requestQueue) == null) {
                dataKeeper.getRequestAndResponseQueues().put(requestQueue, new HashSet<>());
            }

            for (String responseQueue: teachCommand.getQueueNamesToResponses()) {
                dataKeeper.getRequestAndResponseQueues().get(requestQueue).add(responseQueue);
            }
        }

        if (teachCommand.getNamesOfJSONObjToCompare() == null) {
            // Add listeners for each request queue
            for (String requestQueue: teachCommand.getQueueNamesToRequests()) {
                if (!dataKeeper.getQueuesNowAreListened().contains(requestQueue))
                    rabbitMQWrapper.startListenQueueAndSendResponses(requestQueue, dataKeeper);
            }
        } else {
            for (String requestQueue: teachCommand.getQueueNamesToRequests()) {
                if (!dataKeeper.getQueuesNowAreListened().contains(requestQueue))
                    rabbitMQWrapper.startListenQueueAndSendResponsesJSON(
                            requestQueue, dataKeeper, jsonKeys, teachCommand.getNamesOfJSONObjToMap());
            }
        }
    }
}
