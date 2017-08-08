# Publish Subscribe RabbitMQ
Simple java application to interact with RabbitMQ.

## Establish connection

All configuration is placed within `/src/main/resources/app.conf` file.

For now there is only one line in this file, that contains URI to connect to RabbitMQ.

#### Example of correct URI:
`amqp://user:password@host:5672/vHost`

## How to write commands

There are two ways to write commands:
* Write commands rigth to console
* Read commands from file. Warning! Each command should be placed in one line.

To read commands from file you should fill in the file placed in `/src/main/resources/commands` before you start the app.


## Commands

### Teach command
This command allows you to teach the program, so it can become smart.

#### Parameters

In this section all parameters of `teach` command are listed. Each parameter has short and full representation.

* `-reqq` (-request_queues) - list of queues separated by commas which contains names of queues to listen.
* `-respq` (-response_queues) - list of queues separated by commas which contains names of queues to send responses.
* `-reqf` (-request_files) - list of files separated by commas which contains names of files to read samples of requests.
* `-respf` (-response_file) - file to read samples of responses.
* `-jc` (-json_compare) - allows to read from files with requests concrete JSON-object and skip all that remains.
* `-jms` (-json_mapping_string) - map strings from request JSON to response JSON.

#### Example 1: 
`teach -reqq queue_req_1 -reqf file_request.json -respq queue_resp_1 -respf file_response.json`

This command will teach the program to reply in concrete queues with concrete response when you obtain concrete requests in concrete queues.

#### Example 2 (with -jc and jms): 
`teach -reqq queue_req_1 -reqf file_request.json -respq queue_resp_1 -respf file_response.json -jc request.requestContent -jms request.requestHeader.requestId,response.responseHeader.requestId`

This command will only compare JSON content that placed inside request.requestContent. To explain this example, if you have JSON like this one:

`{"request":{"requestHeaders": {...}, "requestContent": {...}}}` 

then only content of "requestContent" will be compared in the messages. Moreover, a string with key response.responseHeader.requestId in response will be taken from string with key request.requestHeader.requestId in request.


