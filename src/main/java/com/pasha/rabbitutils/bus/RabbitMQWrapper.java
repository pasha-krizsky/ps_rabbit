package com.pasha.rabbitutils.bus;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * A class that contains a list of methods to interact with RabbitMQ.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
@Getter
public class RabbitMQWrapper {

    private Connection connection;
    private Channel channel;

    public Connection createConnection(String uri)
            throws
            URISyntaxException, NoSuchAlgorithmException, KeyManagementException,
            IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        this.connection = factory.newConnection();
        return connection;
    }

    public Channel createChannel()
            throws IOException {

        this.channel = this.connection.createChannel();
        return this.channel;
    }

    public void publishMessage(String queueName, String message)
            throws IOException {
        channel.basicPublish("", queueName, null, message.getBytes());
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
