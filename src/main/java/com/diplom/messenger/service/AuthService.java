package com.diplom.messenger.service;

import com.diplom.messenger.dto.request.LoginRequest;
import com.diplom.messenger.dto.request.RefreshTokenRequest;
import com.diplom.messenger.dto.request.RegisterRequest;
import com.diplom.messenger.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);
}