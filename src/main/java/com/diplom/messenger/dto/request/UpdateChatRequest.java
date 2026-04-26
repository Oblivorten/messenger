package com.diplom.messenger.dto.request;

import lombok.Data;

@Data
public class UpdateChatRequest {
    private String name;
    private String avatarUrl;
}