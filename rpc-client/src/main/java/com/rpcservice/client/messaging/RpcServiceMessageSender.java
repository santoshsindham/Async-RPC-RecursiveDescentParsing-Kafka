package com.rpcservice.client.messaging;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class RpcServiceMessageSender {
    
    private static int messageConunter =0;
    
    private List<String> messages = new ArrayList<>();
    
    private static final Logger logger = Loggers.getLogger(RpcServiceMessageSender.class);

    @Autowired
    private KafkaSender<String, String> rpcMessageSender;
    
    @Value("${kafka.rpcservice.topic}")
    private String topic;
    
    @PostConstruct
    public void initHandlerServiceCache() {
        messages.add("1+2+3;.");
        messages.add("1+2*3;.");
        messages.add("1+2*3+4;.");
        messages.add("1abc");
    }
    
    
   public Flux<String> sendMessage(){
       if(messageConunter > 3) {
           messageConunter = 0;
       }
       String messageToSend = messages.get(messageConunter);
       messageConunter++;
       
       return rpcMessageSender
        .send(Mono.just(createSenderRecord(messageToSend, Integer.toString(messageConunter))))
        .map(sendResult -> sendResult.correlationMetadata())
        .doOnNext(correlationMetadata -> logger.info("Message has been sent successfully with correlationMetadata id {}", correlationMetadata))
        .doOnError(error -> logger.info("Error while sending message to rpc servie {}", error));
       
       
   }

   

    private SenderRecord<String, String, String> createSenderRecord(String messageToPublish, String key) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key,
                messageToPublish);
        return SenderRecord.create(producerRecord, key);

    }
}
