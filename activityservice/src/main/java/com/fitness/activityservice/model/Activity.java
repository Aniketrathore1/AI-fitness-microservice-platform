package com.fitness.activityservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// @Document maps this class to a MongoDB collection called "activities"
@Document(collection = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    private String id;

    private String userId;
    private String type;
    private Integer duration;
    private Double caloriesBurned;
    private String notes;


    @CreatedDate
    private LocalDateTime activityDate;

    @CreatedDate
    private LocalDateTime createdAt;
}
