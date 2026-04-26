package com.diplom.messenger.dto.response;

import com.diplom.messenger.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private String avatarUrl;

    private Status status;

    private LocalDateTime lastSeen;
 }
