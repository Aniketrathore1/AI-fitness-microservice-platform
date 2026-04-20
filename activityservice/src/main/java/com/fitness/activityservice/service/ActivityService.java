package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityDto;
import com.fitness.activityservice.messaging.ActivityProducer;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityProducer activityProducer;
    private final WebClient.Builder webClientBuilder;


    public ActivityDto.ActivityResponse logActivity(ActivityDto.ActivityRequest request) {

        boolean userExists = webClientBuilder.build()
                .get()
                .uri("http://userservice/api/users/{userId}/exists", request.getUserId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(userExists)) {
            throw new RuntimeException("User not found: " + request.getUserId());
        }


        Activity activity = new Activity();
        activity.setUserId(request.getUserId());
        activity.setType(request.getType());
        activity.setDuration(request.getDuration());
        activity.setCaloriesBurned(request.getCaloriesBurned());
        activity.setNotes(request.getNotes());
        activity.setActivityDate(LocalDateTime.now());
        activity.setCreatedAt(LocalDateTime.now());

        Activity savedActivity = activityRepository.save(activity);
        log.info("Activity saved to MongoDB: {}", savedActivity.getId());


        activityProducer.sendActivity(savedActivity);

        return mapToResponse(savedActivity);
    }


    public List<ActivityDto.ActivityResponse> getActivitiesByUser(String userId) {
        return activityRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public ActivityDto.ActivityResponse getActivityById(String activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found: " + activityId));
        return mapToResponse(activity);
    }

    private ActivityDto.ActivityResponse mapToResponse(Activity activity) {
        ActivityDto.ActivityResponse response = new ActivityDto.ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setNotes(activity.getNotes());
        response.setActivityDate(activity.getActivityDate() != null ? activity.getActivityDate().toString() : null);
        response.setCreatedAt(activity.getCreatedAt() != null ? activity.getCreatedAt().toString() : null);
        return response;
    }
}
