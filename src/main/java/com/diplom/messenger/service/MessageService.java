package com.diplom.messenger.service;

import com.diplom.messenger.dto.request.SendMessageRequest;
import com.diplom.messenger.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getMessages(String username, Long chatId, Long cursorId, int limit);
    void deleteMessage(String username, Long messageId);
    MessageResponse editMessage(String username, Long messageId, SendMessageRequest request);
    void markAsRead(String username, Long messageId);
    MessageResponse sendMessage(String username, Long chatId, SendMessageRequest request);
}