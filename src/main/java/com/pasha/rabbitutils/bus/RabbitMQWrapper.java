package com.pasha.rabbitutils.bus;

import com.pasha.rabbitutils.keeper.DataKeeper;
import com.pasha.rabbitutils.util.JSONMapper;
import com.rabbitmq.client.*;
import lombok.Getter;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * A class that contains a list of methods to interact with RabbitMQ.
 *
 * Created by Pavel.Krizskiy on 8/6/2017.
 */
@Getter
public class RabbitMQWrapper {

    /** Connection to RabbitMQ. */
    private Connection connection;

    /** Channel. */
    private Channel channel;

    /** Creates and returns new connection to RabbitMQ. */
    public Connection createConnection(String uri) {

        ConnectionFactory factory = new ConnectionFactory();

        try {
            factory.setUri(uri);
            this.connection = factory.newConnection();
        } catch (Exception e) {
            System.out.println("Cannot create connection to RabbitMQ");
            e.printStackTrace();
        }

        return this.connection;
    }

    /** Creates new channel. */
    public Channel createChannel() {

        try {
            this.channel = this.connection.createChannel();
        } catch (IOException e) {
            System.out.println("Cannot create channel");
            e.printStackTrace();
        }

        return this.channel;
    }

    /** Sends message without properties right to the queue. */
    public void publishMessage(String queueName, String message) {

        try {
            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println("Message was sent!");
        } catch (IOException e) {
            System.out.println("Cannot publish message to queue: " + queueName + " with body: " + message);
            e.printStackTrace();
        }
    }

    /** Listens to queue and reply with prepared answers. */
    public void startListenQueueAndSendResponses(String queueName, DataKeeper dataKeeper) {

        System.out.println("Start listening to queue " + queueName + "...");

        dataKeeper.getQueuesNowAreListened().add(queueName);

        // New consumer
        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {

                // Get message from the queue
                String message = new String(body, "UTF-8");
                System.out.println("Got message!");
                System.out.println();

                if (!dataKeeper.getQueueAndRequests().get(queueName).contains(message)) {
                    System.out.println("Unknown message. Use 'teach' command first");
                } else {

                    // Select all queues to response to concrete request
                    for (String queueToResponse: dataKeeper.getRequestAndResponseQueues().get(queueName)) {
                        int index = dataKeeper.getQueueAndRequests().get(queueName).indexOf(message);
                        String messageToResponse = dataKeeper.getQueueAndResponses().get(queueToResponse).get(index);
                        publishMessage(queueToResponse, messageToResponse);
                        System.out.println("Response was sent to " + queueToResponse);
                    }
                }

                this.handleCancelOk("accepted");
            }
        };

        try {
            // Listen
            channel.basicConsume(queueName, true, consumer);

        } catch (IOException e) {
            System.out.println("Cannot receive message from queue " + queueName);
        }
    }

    /** Listens to queue and reply with prepared answers. */
    public void startListenQueueAndSendResponsesJSON(
            String queueName,
            DataKeeper dataKeeper,
            List<String> pathToJSONObjectToCompare,
            List<String> listOfJSONObjectsToMap
    ) {

        // TODO: add different pathToJSONObject for one queue

        System.out.println("Start listening to queue " + queueName + "...");

        dataKeeper.getQueuesNowAreListened().add(queueName);

        // New consumer
        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {

                // Get message from the queue
                String requestMessage = new String(body, "UTF-8");
                String fullRequestMessage = new String(requestMessage);

                JSONObject json = new JSONObject(requestMessage);
                json = (JSONObject) json.get(pathToJSONObjectToCompare.get(0));

                for (int i = 1; i < pathToJSONObjectToCompare.size(); ++i) {
                    json = (JSONObject) json.get(pathToJSONObjectToCompare.get(i));
                }

                // Required part of request message to find proper response
                requestMessage = json.toString();
                System.out.println("Got message!");
                System.out.println();

                if (!dataKeeper.getQueueAndRequests().get(queueName).contains(requestMessage)) {
                    System.out.println("Unknown message. Use 'teach' command first");
                } else {

                    for (String queueToResponse: dataKeeper.getRequestAndResponseQueues().get(queueName)) {

                        int index = dataKeeper.getQueueAndRequests().get(queueName).indexOf(requestMessage);
                        String messageToResponse = dataKeeper.getQueueAndResponses().get(queueToResponse).get(index);

                        String preparedMessage = messageToResponse;
                        if (listOfJSONObjectsToMap != null) {
                            preparedMessage = JSONMapper.throwStringFromRequest(listOfJSONObjectsToMap, messageToResponse, fullRequestMessage);
                        }

                        publishMessage(queueToResponse, preparedMessage);
                        System.out.println("Response was sent to " + queueToResponse);
                    }

                }

                this.handleCancelOk("accepted");
            }
        };

        try {
            // Listen
            channel.basicConsume(queueName, true, consumer);

        } catch (IOException e) {
            System.out.println("Cannot receive message from queue " + queueName);
            e.printStackTrace();
        }
    }

    /** Closes channel and connection. */
    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
