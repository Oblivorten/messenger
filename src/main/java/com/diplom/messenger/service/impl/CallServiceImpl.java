package com.diplom.messenger.service.impl;

import com.diplom.messenger.config.LiveKitConfig;
import com.diplom.messenger.dto.request.InitiateCallRequest;
import com.diplom.messenger.dto.response.CallHistoryResponse;
import com.diplom.messenger.dto.response.CallResponse;
import com.diplom.messenger.dto.websocket.EventType;
import com.diplom.messenger.entity.*;
import com.diplom.messenger.repository.*;
import com.diplom.messenger.service.CallService;
import com.diplom.messenger.service.LiveKitService;
import com.diplom.messenger.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CallServiceImpl implements CallService {

    private final CallRecordRepository callRecordRepository;
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserRepository userRepository;
    private final LiveKitService liveKitService;
    private final LiveKitConfig liveKitConfig;
    private final WebSocketService webSocketService;

    @Override
    @Transactional
    public CallResponse initiateCall(String username, InitiateCallRequest request) {
        User initiator = getUser(username);
        Chat chat = chatRepository.findById(request.getChatId())
                .orElseThrow(() -> new RuntimeException("Чат не найден"));

        if (!chatMemberRepository.existsByChatAndUser(chat, initiator)) {
            throw new RuntimeException("Нет доступа к чату");
        }

        CallRecord call = CallRecord.builder()
                .chat(chat)
                .initiator(initiator)
                .type(request.getType())
                .status(CallStatus.MISSED)
                .build();

        callRecordRepository.save(call);

        try {
            String roomName = liveKitService.getRoomName(call.getId());
            String token = liveKitService.generateToken(username, roomName);

            webSocketService.sendToChat(chat.getId(), EventType.CALL_INCOMING,
                    Map.of(
                            "callId", call.getId(),
                            "initiatorUsername", username,
                            "type", request.getType().name(),
                            "chatId", chat.getId()
                    ));

            return CallResponse.builder()
                    .callId(call.getId())
                    .livekitToken(token)
                    .livekitUrl(liveKitConfig.url)
                    .type(call.getType().name())
                    .status(call.getStatus().name())
                    .startedAt(call.getStartedAt())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания звонка: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public CallResponse acceptCall(String username, Long callId) {
        CallRecord call = getCall(callId);
        User user = getUser(username);

        if (!chatMemberRepository.existsByChatAndUser(call.getChat(), user)) {
            throw new RuntimeException("Нет доступа");
        }

        call.setStatus(CallStatus.ANSWERED);
        callRecordRepository.save(call);

        try {
            String roomName = liveKitService.getRoomName(call.getId());
            String token = liveKitService.generateToken(username, roomName);

            return CallResponse.builder()
                    .callId(call.getId())
                    .livekitToken(token)
                    .livekitUrl(liveKitConfig.url)
                    .type(call.getType().name())
                    .status(call.getStatus().name())
                    .startedAt(call.getStartedAt())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка принятия звонка: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void rejectCall(String username, Long callId) {
        CallRecord call = getCall(callId);
        call.setStatus(CallStatus.REJECTED);
        call.setEndedAt(LocalDateTime.now());
        callRecordRepository.save(call);

        webSocketService.sendToChat(call.getChat().getId(), EventType.CALL_ENDED,
                Map.of("callId", callId, "status", CallStatus.REJECTED.name()));
    }

    @Override
    @Transactional
    public void endCall(String username, Long callId) {
        CallRecord call = getCall(callId);
        call.setEndedAt(LocalDateTime.now());

        if (call.getStartedAt() != null && call.getEndedAt() != null) {
            call.setDuration(ChronoUnit.SECONDS.between(call.getStartedAt(), call.getEndedAt()));
        }

        callRecordRepository.save(call);

        webSocketService.sendToChat(call.getChat().getId(), EventType.CALL_ENDED,
                Map.of("callId", callId, "status", call.getStatus().name(),
                        "duration", call.getDuration() != null ? call.getDuration() : 0));
    }

    @Override
    public List<CallHistoryResponse> getHistory(String username) {
        User user = getUser(username);
        return callRecordRepository.findAllByUser(user)
                .stream()
                .map(this::toHistoryResponse)
                .toList();
    }

    private CallRecord getCall(Long callId) {
        return callRecordRepository.findById(callId)
                .orElseThrow(() -> new RuntimeException("Звонок не найден"));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    private CallHistoryResponse toHistoryResponse(CallRecord call) {
        return CallHistoryResponse.builder()
                .id(call.getId())
                .chatId(call.getChat().getId())
                .initiatorUsername(call.getInitiator().getUsername())
                .type(call.getType().name())
                .status(call.getStatus().name())
                .startedAt(call.getStartedAt())
                .endedAt(call.getEndedAt())
                .duration(call.getDuration())
                .build();
    }
}