package com.diplom.messenger.service;

import com.diplom.messenger.dto.request.RegisterKeyRequest;
import com.diplom.messenger.dto.request.SaveChatKeyRequest;
import com.diplom.messenger.dto.response.ChatKeyResponse;
import com.diplom.messenger.dto.response.KeyPairResponse;
import com.diplom.messenger.dto.response.PublicKeyResponse;

public interface KeyService {
    void registerKeyPair(String username, RegisterKeyRequest request);
    KeyPairResponse getMyKeyPair(String username);
    PublicKeyResponse getPublicKey(Long userId);
    void saveChatKey(String username, Long chatId, SaveChatKeyRequest request);
    ChatKeyResponse getChatKey(String username, Long chatId);
}