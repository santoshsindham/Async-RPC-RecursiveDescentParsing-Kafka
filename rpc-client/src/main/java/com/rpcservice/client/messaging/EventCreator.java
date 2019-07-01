package com.rpcservice.client.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class EventCreator {
    
    private static final Logger logger = Loggers.getLogger(EventCreator.class);

    @Autowired
    private RpcServiceMessageSender rpcServiceMessageSender;

    @Scheduled(fixedRate = 5000)
    public void create() {
        Flux.just("StartSendingMessage")
        .flatMap(message -> rpcServiceMessageSender.sendMessage())
        .doOnNext(message -> logger.info("Message has beend send successfully"))
        .subscribe();      
    }
}
