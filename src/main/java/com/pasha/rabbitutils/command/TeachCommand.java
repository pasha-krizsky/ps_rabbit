package com.pasha.rabbitutils.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.pasha.rabbitutils.converter.StringListConverter;
import com.pasha.rabbitutils.converter.FileListConverter;
import lombok.Getter;

import java.nio.file.Path;
import java.util.List;

/**
 * A class that contains description of "teach" com.pashakrizskiy.ps_rabbit.command.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
@Parameters(
        commandNames = {"teach"},
        commandDescription =
                "This qcommand is aimed to teach this application send correct responses to" +
                "concrete requests."
)
@Getter
public class TeachCommand {

    /** The names of the queues to send requests. */
    @Parameter(
            names = { "-reqq", "-request_queue" },
            listConverter = StringListConverter.class,
            description = "The name of the queue to send requests."
    )
    private List<String> queueNamseToRequests;

    /** The names of the queues to send responses. */
    @Parameter(
            names = { "-respq", "-response_queue" },
            listConverter = StringListConverter.class,
            description = "The name of the queue to send responses."
    )
    private List<String> queueNamesToResponses;

    /** The names of the files with prepared requests. */
    @Parameter(
            names = { "-reqf", "-request_file" },
            listConverter = FileListConverter.class,
            description = "The names of the files with prepared requests."
    )
    private List<Path> fileNamesWithRequests;

    /** The names of the files with prepared responses. */
    @Parameter(
            names = { "-respf", "-response_file" },
            listConverter = FileListConverter.class,
            description = "The names of the files with prepared responses."
    )
    private List<Path> fileNamesWithResponses;
}
