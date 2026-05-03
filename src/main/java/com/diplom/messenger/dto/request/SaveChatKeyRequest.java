package com.diplom.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveChatKeyRequest {
    @NotBlank
    private String encryptedSymmetricKey;
}