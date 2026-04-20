package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityDto;
import com.fitness.activityservice.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityDto.ActivityResponse> logActivity(
            @Valid @RequestBody ActivityDto.ActivityRequest request) {
        return ResponseEntity.ok(activityService.logActivity(request));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityDto.ActivityResponse>> getUserActivities(
            @PathVariable String userId) {
        return ResponseEntity.ok(activityService.getActivitiesByUser(userId));
    }


    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityDto.ActivityResponse> getActivity(
            @PathVariable String activityId) {
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}
