package com.diplom.messenger.service.impl;

import com.diplom.messenger.dto.request.RegisterKeyRequest;
import com.diplom.messenger.dto.request.SaveChatKeyRequest;
import com.diplom.messenger.dto.response.ChatKeyResponse;
import com.diplom.messenger.dto.response.KeyPairResponse;
import com.diplom.messenger.dto.response.PublicKeyResponse;
import com.diplom.messenger.entity.*;
import com.diplom.messenger.repository.*;
import com.diplom.messenger.service.KeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeyServiceImpl implements KeyService {

    private final UserKeyPairRepository userKeyPairRepository;
    private final ChatKeyRepository chatKeyRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;

    @Override
    @Transactional
    public void registerKeyPair(String username, RegisterKeyRequest request) {
        User user = getUser(username);

        if (userKeyPairRepository.existsByUser(user)) {
            throw new RuntimeException("Ключи уже зарегистрированы");
        }

        UserKeyPair keyPair = UserKeyPair.builder()
                .user(user)
                .publicKeyPem(request.getPublicKeyPem())
                .encryptedPrivateKeyPem(request.getEncryptedPrivateKeyPem())
                .build();

        userKeyPairRepository.save(keyPair);
    }

    @Override
    public KeyPairResponse getMyKeyPair(String username) {
        User user = getUser(username);
        UserKeyPair keyPair = userKeyPairRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Ключи не найдены"));

        return KeyPairResponse.builder()
                .publicKeyPem(keyPair.getPublicKeyPem())
                .encryptedPrivateKeyPem(keyPair.getEncryptedPrivateKeyPem())
                .build();
    }

    @Override
    public PublicKeyResponse getPublicKey(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserKeyPair keyPair = userKeyPairRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Ключи пользователя не найдены"));

        return PublicKeyResponse.builder()
                .userId(userId)
                .publicKeyPem(keyPair.getPublicKeyPem())
                .build();
    }

    @Override
    @Transactional
    public void saveChatKey(String username, Long chatId, SaveChatKeyRequest request) {
        User user = getUser(username);
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));

        if (!chatMemberRepository.existsByChatAndUser(chat, user)) {
            throw new RuntimeException("Нет доступа к чату");
        }

        ChatKey chatKey = ChatKey.builder()
                .chat(chat)
                .user(user)
                .encryptedSymmetricKey(request.getEncryptedSymmetricKey())
                .build();

        chatKeyRepository.save(chatKey);
    }

    @Override
    public ChatKeyResponse getChatKey(String username, Long chatId) {
        User user = getUser(username);
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));

        if (!chatMemberRepository.existsByChatAndUser(chat, user)) {
            throw new RuntimeException("Нет доступа к чату");
        }

        ChatKey chatKey = chatKeyRepository.findByChatAndUser(chat, user)
                .orElseThrow(() -> new RuntimeException("Ключ чата не найден"));

        return ChatKeyResponse.builder()
                .chatId(chatId)
                .encryptedSymmetricKey(chatKey.getEncryptedSymmetricKey())
                .build();
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}