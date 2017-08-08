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
* `-jm` (-json_mapping) - allows to read from files with requests concrete JSON-object and skip all that remains.

#### Example 1: 
`teach -reqq req_queue_1,req_queue_2,req_queue_3 -reqf C:\req_file.json -respq resp_queue_1,resp_queue_2 -respf C:\resp_file.json`

This command will teach the program to reply in concrete queues with concrete response when you obtain concrete requests in concrete queues.

#### Example 2 (with -jm): 
`teach -reqq req_queue_1 -reqf C:\req_file.json -respq resp_queue_1 -respf C:\resp_file.json -jm request,requestContent`

This command will only compare JSON content that placed inside request.requestContent. To explain this example, if you have JSON like this one:

`{"request":{"requestHeaders": {...}, "requestContent": {...}}}` 

then only content of "requestContent" will be compared in the messages.
