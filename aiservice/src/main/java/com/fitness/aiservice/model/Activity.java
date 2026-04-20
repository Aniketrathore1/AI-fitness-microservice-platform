package com.fitness.aiservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// This is a plain Java class (not a MongoDB document).
// It mirrors the Activity model in activityservice so Kafka can deserialize the message.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    private String id;
    private String userId;
    private String type;
    private Integer duration;
    private Double caloriesBurned;
    private String notes;
    private LocalDateTime activityDate;
    private LocalDateTime createdAt;
}
