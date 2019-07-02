# Async-RPC-RecursiveDescentParsing-Kafka

This repository has two projects one for the `rpc-client` and other for `rpc-service`

##### Dependencies

- The projects are build using `gradle` and `Spring Boot Version 2.1.6.RELEASE`
- `spring-boot-starter-webflux` is used for Reactive Spring capabilities 
for using `Flux` and `Mono`
- `reactor-kafka` is used for reactively interact with Kafka.

##### Kafka Setup

- Install Kafka by [following the instructions here](https://kafka.apache.org/quickstart)
- Start Zookeeper using 
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```
- Start Kafka using
```bash
bin/kafka-server-start.sh config/server.properties
```
- Client and Server communicate using a topic `rpc-service-topic`, so we shall
create it using the below command
```bash
bin/kafka-topics.sh --zookeeper localhost:2181 --create --replication-factor 1 --partitions 1 --topic rpc-service-topic
```

##### Steps to run the applications

- After cloning the repository `cd` into the `rpc-client` directory and run below 
steps
```bash
cd rpc-client
gradle clean build
```

The above step will download all the required jars and will create an uber 
jar with netty as server in directory `build/libs` inside the `rpc-client` directory.

while still in rpc-client directory execute the below command to start
the rpc-client on netty port 8181

```bash
java -jar build/libs/rpc-client-0.0.1-SNAPSHOT.jar
```

Now we will start the `rpc-server`
```bash
cd rpc-server
gradle clean build
```

The above step will download all the required jars and will create an uber 
jar with netty as server in directory `build/libs` inside the `rpc-server` directory.

while still in rpc-server directory execute the below command to start
the rpc-server on netty port 8080

```bash
java -jar build/libs/rpc-service-0.0.1-SNAPSHOT.jar
```

The `rpc-client` has been scheduled to post string messages reactively 
for Recursive Descent Parsing(RDP) of few pre-configured strings every 5 sec onto Kafka topic
`rpc-service-topic`

The `rpc-server` is configured to listen to the messages from topic 
`rpc-service-topic` reactively and parse the incoming string using RDP and
print the result.

```
   The grammar:

   statement = { expression  ";" } "."
   expression = term { ( "+" | "-" ) term }
   term      = factor { ( "*" | "/" ) factor }
   factor    = number | "(" expression ")"
```

##### Sample Inputs for Parsing:
```

       I/p: 1+2+3;.
       O/p: 6
       
       I/p: 1+2*3;.
       O/p: 7
       
       I/p: 1+2*3+4;.
       O/p: 11
       
       I/p: 1abc
       O/p: The given input is invalid 1abc
```
  
 
