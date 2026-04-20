package com.fitness.aiservice.messaging;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.service.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityMessageListener {

    private final AiRecommendationService aiRecommendationService;

    // Listen on the same topic that ActivityService publishes to
    @KafkaListener(
            topics = "activity-logged",
            groupId = "ai-recommendation-group"
    )
    public void onActivityReceived(Activity activity) {
        log.info("Received activity event from Kafka: activityId={}, userId={}",
                activity.getId(), activity.getUserId());

        // Hand off to service to call Gemini and save recommendation
        aiRecommendationService.processActivity(activity);
    }
}
