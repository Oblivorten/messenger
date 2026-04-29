package com.diplom.messenger.websocket;

import com.diplom.messenger.dto.request.SendMessageRequest;
import com.diplom.messenger.dto.response.MessageResponse;
import com.diplom.messenger.dto.websocket.EventType;
import com.diplom.messenger.service.MessageService;
import com.diplom.messenger.service.WebSocketService;
import com.diplom.messenger.entity.ChatMember;
import com.diplom.messenger.repository.ChatMemberRepository;
import com.diplom.messenger.repository.ChatRepository;
import com.diplom.messenger.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;
    private final WebSocketService webSocketService;
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;

    // Отправить сообщение — клиент шлёт на /app/chat.{chatId}.send
    @MessageMapping("/chat.{chatId}.send")
    public void sendMessage(@DestinationVariable Long chatId,
                            @Payload SendMessageRequest request,
                            Principal principal) {
        MessageResponse message = messageService.sendMessage(principal.getName(), chatId, request);

        webSocketService.sendToChat(chatId, EventType.MESSAGE_NEW, message);
    }

    // Редактировать сообщение — клиент шлёт на /app/message.{messageId}.edit
    @MessageMapping("/message.{messageId}.edit")
    public void editMessage(@DestinationVariable Long messageId,
                            @Payload SendMessageRequest request,
                            Principal principal) {
        MessageResponse message = messageService.editMessage(principal.getName(), messageId, request);

        Chat chat = chatRepository.findById(message.getChatId())
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        webSocketService.sendToChat(chat.getId(), EventType.MESSAGE_EDITED, message);
    }

    // Удалить сообщение — клиент шлёт на /app/message.{messageId}.delete
    @MessageMapping("/message.{messageId}.delete")
    public void deleteMessage(@DestinationVariable Long messageId,
                              Principal principal) {
        messageService.deleteMessage(principal.getName(), messageId);

        webSocketService.sendToChat(messageId, EventType.MESSAGE_DELETED,
                Map.of("messageId", messageId));
    }

    // Прочитать сообщение — клиент шлёт на /app/message.{messageId}.read
    @MessageMapping("/message.{messageId}.read")
    public void markAsRead(@DestinationVariable Long messageId,
                           Principal principal) {
        messageService.markAsRead(principal.getName(), messageId);

        webSocketService.sendToChat(messageId, EventType.MESSAGE_READ,
                Map.of("messageId", messageId, "username", principal.getName()));
    }

    // Индикатор печати — клиент шлёт на /app/chat.{chatId}.typing
    @MessageMapping("/chat.{chatId}.typing")
    public void typing(@DestinationVariable Long chatId,
                       @Payload Map<String, Boolean> payload,
                       Principal principal) {
        boolean isTyping = Boolean.TRUE.equals(payload.get("typing"));
        String eventType = isTyping ? EventType.TYPING_START : EventType.TYPING_STOP;

        webSocketService.sendTyping(chatId, eventType,
                Map.of("username", principal.getName(), "chatId", chatId));
    }
}