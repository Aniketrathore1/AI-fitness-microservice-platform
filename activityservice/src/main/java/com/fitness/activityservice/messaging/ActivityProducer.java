package com.fitness.activityservice.messaging;

import com.fitness.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityProducer {

    private final KafkaTemplate<String, Activity> kafkaTemplate;


    public static final String ACTIVITY_TOPIC = "activity-logged";


    public void sendActivity(Activity activity) {
        log.info("Sending activity to Kafka topic [{}]: activityId={}", ACTIVITY_TOPIC, activity.getId());
        kafkaTemplate.send(ACTIVITY_TOPIC, activity.getUserId(), activity);
    }
}
