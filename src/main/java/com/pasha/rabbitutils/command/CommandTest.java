package com.pasha.rabbitutils.command;

import com.beust.jcommander.JCommander;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

public class CommandTest {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        QueueCommand queueCommand = new QueueCommand();
        TeachCommand teachCommand = new TeachCommand();

        JCommander jc = new JCommander();
        jc.addCommand(queueCommand);
        jc.addCommand(teachCommand);

        args  = new String[] { "queue", "-create", "lol1,lol2,lol3,lol4" };
        jc.parse(args);

        Map<String, Object> headers = new HashMap<>();
        for (String queueName: queueCommand.getQueueNamesToCreate()) {
            channel.queueDeclare(queueName, true, false, false, headers);
        }

        channel.close();
        connection.close();
    }
}
