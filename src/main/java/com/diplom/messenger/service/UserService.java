package com.diplom.messenger.service;

import com.diplom.messenger.dto.request.UpdateUserRequest;
import com.diplom.messenger.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getMe(String username);
    UserResponse updateMe(String username, UpdateUserRequest request);
    List<UserResponse> search(String query);
    String getStatus(Long id);
}