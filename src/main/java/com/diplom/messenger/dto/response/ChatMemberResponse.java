package com.diplom.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChatMemberResponse {
    private Long userId;
    private String username;
    private String role;
    private LocalDateTime joinedAt;
}