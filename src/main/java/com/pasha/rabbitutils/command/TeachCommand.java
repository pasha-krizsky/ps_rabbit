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

    /** The name of the file with prepared response. */
    @Parameter(
            names = { "-respf", "-response_file" },
            description = "The name of the file with prepared response."
    )
    private Path fileNameWithResponse;

    /** JSON object to compare. */
    @Parameter(
            names = { "-jc", "-json_compare" },
            listConverter = StringListConverter.class,
            description = "JSON object to compare."
    )
    private List<String> namesOfJSONObjToCompare;

    /** Map strings from request JSON to response JSON. */
    @Parameter(
            names = { "-jms", "-json_mapping_string" },
            listConverter = StringListConverter.class,
            description = "Map strings from request JSON to response JSON."
    )
    private List<String> namesOfStringsToMap;
}
