package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    // Injected from application.yml
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;


    public String getRecommendation(Activity activity) {
        try {

            String prompt = buildPrompt(activity);

            // Gemini API request body structure
            Map<String, Object> requestBody = Map.of(
                    "contents", new Object[]{
                            Map.of("parts", new Object[]{
                                    Map.of("text", prompt)
                            })
                    }
            );


            String rawResponse = webClient.post()
                    .uri(geminiApiUrl + "?key=" + geminiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse the response JSON and extract the text
            return extractTextFromGeminiResponse(rawResponse);

        } catch (Exception e) {
            log.error("Gemini API call failed for activityId={}: {}", activity.getId(), e.getMessage());
            return "Could not generate recommendation at this time. Please try again later.";
        }
    }

    // Builds a clear, detailed prompt for Gemini
    private String buildPrompt(Activity activity) {
        return String.format(
                """
                You are a professional fitness coach. A user just completed a workout. Provide a short, \
                friendly, and specific recommendation (3-4 sentences) based on the activity below.
                
                Activity Details:
                - Type: %s
                - Duration: %d minutes
                - Calories Burned: %.0f kcal
                - Notes: %s
                
                Give advice on: recovery, next steps, hydration, or how to improve next session.
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned() != null ? activity.getCaloriesBurned() : 0.0,
                activity.getNotes() != null ? activity.getNotes() : "None"
        );
    }

    // Gemini response looks like:
    // { "candidates": [ { "content": { "parts": [ { "text": "..." } ] } } ] }
    private String extractTextFromGeminiResponse(String rawJson) throws Exception {
        JsonNode root = objectMapper.readTree(rawJson);
        return root
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText("No recommendation available.");
    }
}
