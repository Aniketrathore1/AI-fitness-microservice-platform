package com.fitness.aiservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// Each document = one AI recommendation generated for one activity
@Document(collection = "ai_recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommendation {

    @Id
    private String id;

    private String userId;
    private String activityId;
    private String activityType;
    private Integer activityDuration;


    private String recommendation;


    private String status;

    private LocalDateTime createdAt;
}
