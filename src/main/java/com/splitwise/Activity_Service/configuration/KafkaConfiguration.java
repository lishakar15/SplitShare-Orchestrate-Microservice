package com.splitwise.Activity_Service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaConfiguration {
    @Value("${kafka.topic}")
    private String topic;
    @Bean
    public NewTopic getKafkaTopic()
    {
        return TopicBuilder.name(topic).partitions(3).replicas(1).build();
    }
    public String getTopic()
    {
        return topic;
    }
}
