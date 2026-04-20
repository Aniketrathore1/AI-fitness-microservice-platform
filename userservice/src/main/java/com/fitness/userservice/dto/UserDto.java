package com.fitness.userservice.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



public class UserDto {


    @Data
    public static class RegisterRequest {

        @NotBlank(message = "Name is required")
        private String name;

        @Email(message = "Invalid email")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        private Integer age;
        private String fitnessGoal;
        private String fitnessLevel;
    }


    @Data
    public static class UserResponse {
        private String id;
        private String name;
        private String email;
        private Integer age;
        private String fitnessGoal;
        private String fitnessLevel;
        private String createdAt;
    }
}
