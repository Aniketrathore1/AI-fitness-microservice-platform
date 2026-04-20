package com.fitness.aiservice.controller;

import com.fitness.aiservice.model.AiRecommendation;
import com.fitness.aiservice.service.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;


    @GetMapping("/recommendations/user/{userId}")
    public ResponseEntity<List<AiRecommendation>> getUserRecommendations(
            @PathVariable String userId) {
        return ResponseEntity.ok(aiRecommendationService.getRecommendationsByUser(userId));
    }


    @GetMapping("/recommendations/activity/{activityId}")
    public ResponseEntity<AiRecommendation> getRecommendationForActivity(
            @PathVariable String activityId) {
        return ResponseEntity.ok(aiRecommendationService.getRecommendationByActivity(activityId));
    }
}
