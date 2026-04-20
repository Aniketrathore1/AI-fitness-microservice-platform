package com.fitness.activityservice.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ActivityDto {

    // What client sends when logging an activity
    @Data
    public static class ActivityRequest {

        @NotBlank(message = "userId is required")
        private String userId;

        @NotBlank(message = "Activity type is required")
        private String type;   // "RUNNING", "CYCLING", "STRENGTH_TRAINING", "YOGA", etc.

        @NotNull(message = "Duration is required")
        private Integer duration;  // in minutes

        private Double caloriesBurned;
        private String notes;
    }

    // What server returns to client
    @Data
    public static class ActivityResponse {
        private String id;
        private String userId;
        private String type;
        private Integer duration;
        private Double caloriesBurned;
        private String notes;
        private String activityDate;
        private String createdAt;
    }
}
