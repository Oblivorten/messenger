package com.diplom.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChatResponse {
    private Long id;
    private String type;
    private String name;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private Long unreadCount;
}