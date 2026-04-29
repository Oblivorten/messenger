package com.diplom.messenger.service;

import com.diplom.messenger.dto.websocket.EventType;
import com.diplom.messenger.dto.websocket.WebSocketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // Отправить событие всем в чате
    public void sendToChat(Long chatId, String eventType, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/chat." + chatId,
                WebSocketEvent.builder()
                        .type(eventType)
                        .payload(payload)
                        .build()
        );
    }

    // Отправить индикатор печати в чат
    public void sendTyping(Long chatId, String eventType, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/chat." + chatId + ".typing",
                WebSocketEvent.builder()
                        .type(eventType)
                        .payload(payload)
                        .build()
        );
    }

    // Отправить статус пользователя
    public void sendUserStatus(Long userId, String eventType, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/user." + userId + ".status",
                WebSocketEvent.builder()
                        .type(eventType)
                        .payload(payload)
                        .build()
        );
    }

    // Отправить личное уведомление пользователю
    public void sendNotification(Long userId, String eventType, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/user." + userId + ".notify",
                WebSocketEvent.builder()
                        .type(eventType)
                        .payload(payload)
                        .build()
        );
    }
}