package com.diplom.messenger.service;

import com.diplom.messenger.dto.response.NotificationResponse;
import com.diplom.messenger.entity.NotificationType;
import com.diplom.messenger.entity.User;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getMyNotifications(String username);
    void markAllAsRead(String username);
    void createNotification(User user, NotificationType type, String content);
}