package com.splitwise.Activity_Service.kafka;

import com.splitwise.Activity_Service.configuration.KafkaConfiguration;
import com.splitwise.Activity_Service.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @Autowired
    KafkaConfiguration kafkaConfiguration;
    @Autowired
    ActivityService activityService;

    @KafkaListener(topics = "activity",groupId = "myGroup")
    public void consumeActivities(String activityMsg)
    {
        activityService.saveActivity(activityMsg);
    }
}
