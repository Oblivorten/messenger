package com.diplom.messenger.websocket;

import com.diplom.messenger.dto.websocket.EventType;
import com.diplom.messenger.entity.Status;
import com.diplom.messenger.entity.User;
import com.diplom.messenger.repository.ChatMemberRepository;
import com.diplom.messenger.repository.UserRepository;
import com.diplom.messenger.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserRepository userRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final WebSocketService webSocketService;

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        String username = event.getUser() != null ? event.getUser().getName() : null;
        if (username == null) return;

        userRepository.findByUsername(username).ifPresent(user -> {
            user.setStatus(Status.ONLINE);
            userRepository.save(user);

            webSocketService.sendUserStatus(user.getId(), EventType.USER_ONLINE,
                    Map.of("userId", user.getId(), "username", username));
        });
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String username = event.getUser() != null ? event.getUser().getName() : null;
        if (username == null) return;

        userRepository.findByUsername(username).ifPresent(user -> {
            user.setStatus(Status.OFFLINE);
            user.setLastSeen(LocalDateTime.now());
            userRepository.save(user);

            webSocketService.sendUserStatus(user.getId(), EventType.USER_OFFLINE,
                    Map.of("userId", user.getId(), "username", username,
                            "lastSeen", user.getLastSeen().toString()));
        });
    }
}