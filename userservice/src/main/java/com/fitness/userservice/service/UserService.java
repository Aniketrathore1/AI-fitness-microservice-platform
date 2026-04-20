package com.fitness.userservice.service;

import com.fitness.userservice.dto.UserDto;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto.UserResponse registerUser(UserDto.RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setAge(request.getAge());
        user.setFitnessGoal(request.getFitnessGoal());
        user.setFitnessLevel(request.getFitnessLevel());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public UserDto.UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return mapToResponse(user);
    }

    public boolean userExists(String userId) {
        return userRepository.existsById(userId);
    }

    private UserDto.UserResponse mapToResponse(User user) {
        UserDto.UserResponse response = new UserDto.UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setAge(user.getAge());
        response.setFitnessGoal(user.getFitnessGoal());
        response.setFitnessLevel(user.getFitnessLevel());
        response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        return response;
    }
}
