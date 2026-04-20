package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.AiRecommendation;
import com.fitness.aiservice.repository.AiRecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiRecommendationService {

    private final AiRecommendationRepository recommendationRepository;
    private final GeminiService geminiService;

    public void processActivity(Activity activity) {
        log.info("Processing activity for AI recommendation: activityId={}, type={}",
                activity.getId(), activity.getType());

        AiRecommendation recommendation = new AiRecommendation();
        recommendation.setUserId(activity.getUserId());
        recommendation.setActivityId(activity.getId());
        recommendation.setActivityType(activity.getType());
        recommendation.setActivityDuration(activity.getDuration());
        recommendation.setCreatedAt(LocalDateTime.now());

        try {
            String aiText = geminiService.getRecommendation(activity);
            recommendation.setRecommendation(aiText);
            recommendation.setStatus("SUCCESS");
            log.info("AI recommendation generated for activityId={}", activity.getId());
        } catch (Exception e) {
            recommendation.setRecommendation("Unable to generate recommendation.");
            recommendation.setStatus("FAILED");
            log.error("Failed to get AI recommendation: {}", e.getMessage());
        }

        // Save to MongoDB
        recommendationRepository.save(recommendation);
    }


    public List<AiRecommendation> getRecommendationsByUser(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    // Get recommendation for one specific activity
    public AiRecommendation getRecommendationByActivity(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException(
                        "No recommendation found for activityId: " + activityId));
    }
}
