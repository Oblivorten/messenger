package com.diplom.messenger.service.impl;

import com.diplom.messenger.dto.request.SendMessageRequest;
import com.diplom.messenger.dto.response.MessageResponse;
import com.diplom.messenger.entity.*;
import com.diplom.messenger.repository.*;
import com.diplom.messenger.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserRepository userRepository;
    private final MessageReadRepository messageReadRepository;

    @Override
    public List<MessageResponse> getMessages(String username, Long chatId, Long cursorId, int limit) {
        Chat chat = getChat(chatId);
        checkMember(chat, username);

        List<Message> messages;
        PageRequest pageable = PageRequest.of(0, limit);

        if (cursorId == null) {
            messages = messageRepository.findByChatNoCursor(chat, pageable);
        } else {
            messages = messageRepository.findByChatWithCursor(chat, cursorId, pageable);
        }

        List<MessageResponse> result = messages.stream()
                .map(this::toResponse)
                .toList();

        Collections.reverse((List<?>) result);
        return result;
    }

    @Override
    @Transactional
    public void deleteMessage(String username, Long messageId) {
        Message message = getMessage(messageId);
        if (!message.getSender().getUsername().equals(username)) {
            throw new RuntimeException("Нельзя удалить чужое сообщение");
        }
        message.setDeletedAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public MessageResponse editMessage(String username, Long messageId, SendMessageRequest request) {
        Message message = getMessage(messageId);
        if (!message.getSender().getUsername().equals(username)) {
            throw new RuntimeException("Нельзя редактировать чужое сообщение");
        }
        if (message.getDeletedAt() != null) {
            throw new RuntimeException("Нельзя редактировать удалённое сообщение");
        }
        message.setEncryptedContent(request.getEncryptedContent());
        message.setIvHex(request.getIvHex());
        message.setEditedAt(LocalDateTime.now());
        messageRepository.save(message);
        return toResponse(message);
    }

    @Override
    @Transactional
    public void markAsRead(String username, Long messageId) {
        Message message = getMessage(messageId);
        User user = getUser(username);
        if (!messageReadRepository.existsByMessageAndUser(message, user)) {
            MessageRead read = MessageRead.builder()
                    .message(message)
                    .user(user)
                    .build();
            messageReadRepository.save(read);
        }
    }

    private Message getMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Сообщение не найдено"));
    }

    private Chat getChat(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    private void checkMember(Chat chat, String username) {
        User user = getUser(username);
        if (!chatMemberRepository.existsByChatAndUser(chat, user)) {
            throw new RuntimeException("Нет доступа к этому чату");
        }
    }

    private MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .chatId(message.getChat().getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .type(message.getType().name())
                .encryptedContent(message.getEncryptedContent())
                .ivHex(message.getIvHex())
                .replyToId(message.getReplyTo() != null ? message.getReplyTo().getId() : null)
                .editedAt(message.getEditedAt())
                .deletedAt(message.getDeletedAt())
                .createdAt(message.getCreatedAt())
                .build();
    }
}