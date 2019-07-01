package com.decent.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

@Component
public class KafkaConfig {

    @Value("#{${rpcservice.messaging.kafka.config}}")
    private Map<String,String> kafkaConfig;

    @Value("${kafka.rpcservice.topic}")
    private String topic;
    
    protected Map<String, Object> populateReceiverConfigs(Map<String, Object> props, Map<String, String> kafkaConfig){
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.get("kafka.hosts"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.get("group.id"));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfig.get("offset.reset"));
        return props;
    }
    
    
    @Bean
    public Map<String, Object> rpcConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        populateReceiverConfigs(props, kafkaConfig);
        return props;
    }
    
    @Bean
    public KafkaReceiver<String, String> rpcMessageReceiver() {
        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.<String, String>create(rpcConsumerConfigs())
                .subscription(Collections.singleton(topic));
        return KafkaReceiver.create(receiverOptions);
    }
}
