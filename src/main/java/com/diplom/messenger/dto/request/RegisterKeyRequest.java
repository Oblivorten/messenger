package com.diplom.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterKeyRequest {
    @NotBlank
    private String publicKeyPem;
    @NotBlank
    private String encryptedPrivateKeyPem;
}