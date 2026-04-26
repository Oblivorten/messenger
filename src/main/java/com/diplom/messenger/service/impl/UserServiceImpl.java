package com.diplom.messenger.service.impl;

import com.diplom.messenger.dto.request.UpdateUserRequest;
import com.diplom.messenger.dto.response.UserResponse;
import com.diplom.messenger.entity.User;
import com.diplom.messenger.repository.UserRepository;
import com.diplom.messenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getMe(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return toResponse(user);
    }

    @Override
    public UserResponse updateMe(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setUsername(request.getUsername());
        user.setAvatarUrl(request.getAvatarUrl());
        userRepository.save(user);
        return toResponse(user);
    }

    @Override
    public List<UserResponse> search(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public String getStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return user.getStatus().name();
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .lastSeen(user.getLastSeen())
                .build();
    }
}