package com.pasha.rabbitutils.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.pasha.rabbitutils.converter.StringListConverter;
import lombok.Getter;

import java.util.List;

/**
 * A class that contains description of "queue" command.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
@Parameters(
        commandNames = {"queue"},
        commandDescription = "This command is aimed to work with queues."
)
@Getter
public class QueueCommand implements ICommand {

    /** The names of the queues to create. */
    @Parameter(
            names = { "-c", "-create" },
            listConverter = StringListConverter.class,
            description = "Creates the queues in RabbitMQ."
    )
    private List<String> queueNamesToCreate;

    /** The names of the queues to delete. */
    @Parameter(
            names = { "-rm", "-remove" },
            listConverter = StringListConverter.class,
            description = "Removes the queues from RabbitMQ."
    )
    private List<String> queueNamesToDelete;

    /** The names of the queues to read messages. */
    @Parameter(
            names = { "-rd", "-read" },
            listConverter = StringListConverter.class,
            description = "Start listening the queues to read messages."
    )
    private List<String> queueNamesToRead;

    /** The names of the queues to write messages. */
    @Parameter(
            names = { "-wr", "-write" },
            listConverter = StringListConverter.class,
            description = "Write messages right to the queues."
    )
    private List<String> queueNamesToWrite;

    /** The message to send. */
    @Parameter(
            names = { "-msg", "-message" },
            listConverter = StringListConverter.class,
            description = "The message to send."
    )
    private List<String> messageToSend;

    /** The names of the queues to stop reading messages. */
    @Parameter(
            names = { "-sr", "-stop_read" },
            listConverter = StringListConverter.class,
            description = "Stop reading messages from the queues."
    )
    private List<String> queueNamesToStopRead;

    /** The names of the queue to stop write messages. */
    @Parameter(
            names = { "-sw", "-stop_write" },
            listConverter = StringListConverter.class,
            description = "Stop writing messages to the queues."
    )
    private List<String> queueNamesToStopWrite;
}
