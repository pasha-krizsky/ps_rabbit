package com.pasha.rabbitutils.bus;

import com.pasha.rabbitutils.keeper.DataKeeper;
import com.rabbitmq.client.*;
import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
    public Connection createConnection(String uri)
            throws
            URISyntaxException, NoSuchAlgorithmException, KeyManagementException,
            IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        this.connection = factory.newConnection();
        return this.connection;
    }

    /** Creates new channel. */
    public Channel createChannel()
            throws IOException {

        this.channel = this.connection.createChannel();
        return this.channel;
    }

    /** Sends message without properties right to the queue. */
    public void publishMessage(String queueName, String message)
            throws IOException {
        channel.basicPublish("", queueName, null, message.getBytes());
    }

    /** Listens to queue and reply with prepared answers. */
    public void startListenQueueAndSendResponses(String queueName, DataKeeper dataKeeper) {

        System.out.println("Start listening to queue " + queueName + "...");

        // New consumer
        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {

                // Get message from the queue
                String message = new String(body, "UTF-8");
                System.out.println("Got message:");
                System.out.println(message);

                // Do we know this request message?
                if (!dataKeeper.getQueueAndRequests().get(queueName).contains(message)) {
                    System.out.println("There is no message with such body. Use 'teach' command first");
                } else {

                    // Maybe we know in which queue should we reply?
                    if (dataKeeper.getRequestAndResponseQueues().get(queueName).isEmpty()) {
                        System.out.println("No queues to send response. Use 'teach command first'");
                    } else {

                        // Select all queues to response to concrete request
                        for (String queueToResponse: dataKeeper.getRequestAndResponseQueues().get(queueName)) {

                            // Send message to all queues
                            for (String messageToResponse: dataKeeper.getQueueAndResponses().get(queueToResponse)) {
                                publishMessage(queueToResponse, messageToResponse);
                            }
                        }
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
