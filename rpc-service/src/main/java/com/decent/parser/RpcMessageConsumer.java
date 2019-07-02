package com.decent.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import reactor.core.Disposable;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class RpcMessageConsumer {

    private static final Logger logger = Loggers.getLogger(RpcMessageConsumer.class);

    @Autowired
    private KafkaReceiver<String, String> rpcMessageReceiver;

    @Autowired
    private Parser parser;

    @EventListener(ApplicationReadyEvent.class)
    public Disposable consumeMessage() {
        return rpcMessageReceiver.receive()
                .map(receiverRecord -> parseUsingDecentRecursive(receiverRecord))
                .doOnNext(parsedValue -> logger.info("Recursive Descent Parsing result is: {}", parsedValue))
                .subscribe();

    }
    
    private String parseUsingDecentRecursive(ReceiverRecord<String, String> receiverRecord) {
        logger.info("The incoming string for parsing is: {}", receiverRecord.value());
        receiverRecord.receiverOffset().acknowledge();
        return parser.parse(receiverRecord.value());
    }
}
