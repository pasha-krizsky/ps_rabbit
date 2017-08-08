package com.pasha.rabbitutils.bus;

import com.pasha.rabbitutils.keeper.DataKeeper;
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
        } catch (IOException e) {
            System.out.println("Cannot publish message to queue: " + queueName + " with body: " + message);
            e.printStackTrace();
        }
    }

    /** Method for listeners. */
    private void commonListen(DataKeeper dataKeeper, String queueName, String message) {
        // Do we know this request message?
        if (!dataKeeper.getQueueAndRequests().get(queueName).contains(message)) {
            System.out.println("Unknown message. Use 'teach' command first");
        } else {

            // Maybe we know in which queue should we reply?
            if (dataKeeper.getRequestAndResponseQueues().get(queueName).isEmpty()) {
                System.out.println("No queues to send response. Use 'teach command first'");
            } else {

                // Select all queues to response to concrete request
                for (String queueToResponse: dataKeeper.getRequestAndResponseQueues().get(queueName)) {

                    int index = dataKeeper.getQueueAndRequests().get(queueName).indexOf(message);
                    String messageToResponse = dataKeeper.getQueueAndResponses().get(queueToResponse).get(index);
                    publishMessage(queueToResponse, messageToResponse);

                    System.out.println("Message was processed!");
                    System.out.println("Response was sent to " + queueToResponse);

                    // Send message to all queues
//                    for (String messageToResponse: dataKeeper.getQueueAndResponses().get(queueToResponse)) {
//                        publishMessage(queueToResponse, messageToResponse);
//                        System.out.println("Message was processed!");
//                        System.out.println("Response was sent to " + queueToResponse);
//                    }
                }
            }
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

                commonListen(dataKeeper, queueName, message);

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
            List<String> pathToJSONObject
    ) {

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

                JSONObject json = new JSONObject(message);
                json = (JSONObject) json.get(pathToJSONObject.get(0));

                for (int i = 1; i < pathToJSONObject.size(); ++i) {
                    json = (JSONObject) json.get(pathToJSONObject.get(i));
                }

                message = json.toString();
                System.out.println("Got message!");
                System.out.println();

                commonListen(dataKeeper, queueName, message);

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
