package com.diplom.messenger.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MuteChatRequest {
    private LocalDateTime mutedUntil;
}