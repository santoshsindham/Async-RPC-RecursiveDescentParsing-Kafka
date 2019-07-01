package com.rpcservice.client.messaging;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

@Component
public class KafkaConfig {

    @Value("#{${rpcservice.messaging.kafka.config}}")
    private Map<String,String> kafkaConfig;

    @Value("${kafka.rpcservice.topic}")
    private String topic;
    
    protected Map<String, Object> populateSenderConfigs(Map<String, Object> props, Map<String, String> kafkaConfig) {
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.get("kafka.hosts"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaConfig.get("kafka.timeout"));
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaConfig.get("batch.size"));
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaConfig.get("linger.ms"));
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaConfig.get("buffer.memory"));
        props.put(ProducerConfig.ACKS_CONFIG, kafkaConfig.get("kafka.acks"));
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaConfig.get("kafka.retries"));
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaConfig.get("kafka.max-inflight"));
        return props;
    }

    
    @Bean
    public KafkaSender<String, String> rpcMessageSender() {
        Map<String, Object> props = new HashMap<>();
        populateSenderConfigs(props, kafkaConfig);
        SenderOptions<String, String> senderOptions = SenderOptions.create(props);
        return KafkaSender.create(senderOptions);
    }
    
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
  
    
}
