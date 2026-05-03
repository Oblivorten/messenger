package com.diplom.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatKeyResponse {
    private Long chatId;
    private String encryptedSymmetricKey;
}