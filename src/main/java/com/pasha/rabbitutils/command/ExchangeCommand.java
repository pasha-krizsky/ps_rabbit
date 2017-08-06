package com.pasha.rabbitutils.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.pasha.rabbitutils.converter.StringListConverter;
import lombok.Getter;

import java.util.List;

/**
 * A class that contains description of "exchange" command.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
@Parameters(
        commandNames = {"exchange"},
        commandDescription = "This command is aimed to work with exchanges in RabbitMQ."
)
@Getter
public class ExchangeCommand {

    /** The names of the exchanges to create. */
    @Parameter(
            names = { "-c", "-create" },
            listConverter = StringListConverter.class,
            description = "Creates new queues in RabbitMQ."
    )
    private List<String> exchangeNamesToCreate;

    /** The names of the exchanges to delete. */
    @Parameter(
            names = { "-rm", "-remove" },
            listConverter = StringListConverter.class,
            description = "Removes the exchanges from RabbitMQ."
    )
    private List<String> exchangeNamesToDelete;
}
