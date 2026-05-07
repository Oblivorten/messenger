package com.diplom.messenger.service.impl;

import com.diplom.messenger.dto.response.NotificationResponse;
import com.diplom.messenger.entity.*;
import com.diplom.messenger.repository.NotificationRepository;
import com.diplom.messenger.repository.UserRepository;
import com.diplom.messenger.service.NotificationService;
import com.diplom.messenger.service.WebSocketService;
import com.diplom.messenger.dto.websocket.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final WebSocketService webSocketService;

    @Override
    public List<NotificationResponse> getMyNotifications(String username) {
        User user = getUser(username);
        return notificationRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void markAllAsRead(String username) {
        User user = getUser(username);
        notificationRepository.markAllAsRead(user);
    }

    @Override
    @Transactional
    public void createNotification(User user, NotificationType type, String content) {
        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .content(content)
                .build();

        notificationRepository.save(notification);

        webSocketService.sendNotification(user.getId(), EventType.MESSAGE_NEW,
                Map.of(
                        "type", type.name(),
                        "content", content
                ));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType().name())
                .content(notification.getContent())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}