package com.pasha.rabbitutils.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.pasha.rabbitutils.converter.StringListConverter;
import com.pasha.rabbitutils.converter.FileListConverter;
import lombok.Getter;

import java.nio.file.Path;
import java.util.List;

/**
 * A class that contains description of "teach" command.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
@Parameters(
        commandNames = {"teach"},
        commandDescription =
                "This command is aimed to teach this application send correct responses to" +
                "concrete requests."
)
@Getter
public class TeachCommand {

    /** The names of the queues to send requests. */
    @Parameter(
            names = { "-reqq", "-request_queues" },
            listConverter = StringListConverter.class,
            description = "The name of the queue to send requests."
    )
    private List<String> queueNamesToRequests;

    /** The names of the queues to send responses. */
    @Parameter(
            names = { "-respq", "-response_queues" },
            listConverter = StringListConverter.class,
            description = "The name of the queue to send responses."
    )
    private List<String> queueNamesToResponses;

    /** The names of the files with prepared requests. */
    @Parameter(
            names = { "-reqf", "-request_files" },
            listConverter = FileListConverter.class,
            description = "The names of the files with prepared requests."
    )
    private List<Path> fileNamesWithRequests;

    /** The names of the files with prepared responses. */
    @Parameter(
            names = { "-respf", "-response_files" },
            listConverter = FileListConverter.class,
            description = "The names of the files with prepared responses."
    )
    private List<Path> fileNamesWithResponses;

    /** JSON object to compare. */
    @Parameter(
            names = { "-jc", "-json_compare" },
            listConverter = StringListConverter.class,
            description = "JSON object to compare."
    )
    private List<String> namesOfJSONObjToCompare;

    /** JSON object to map. */
    @Parameter(
            names = { "-jm", "-json_mapping" },
            listConverter = StringListConverter.class,
            description = "JSON object to map."
    )
    private List<String> namesOfJSONObjToMap;
}
