package com.fitness.aiservice.repository;

import com.fitness.aiservice.model.AiRecommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiRecommendationRepository extends MongoRepository<AiRecommendation, String> {


    List<AiRecommendation> findByUserId(String userId);


    Optional<AiRecommendation> findByActivityId(String activityId);
}
