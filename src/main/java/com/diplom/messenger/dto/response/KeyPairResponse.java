package com.diplom.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KeyPairResponse {
    private String publicKeyPem;
    private String encryptedPrivateKeyPem;
}