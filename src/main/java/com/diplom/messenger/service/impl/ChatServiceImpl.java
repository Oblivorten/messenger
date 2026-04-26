package com.diplom.messenger.service.impl;

import com.diplom.messenger.dto.request.*;
import com.diplom.messenger.dto.response.*;
import com.diplom.messenger.entity.*;
import com.diplom.messenger.repository.*;
import com.diplom.messenger.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public ChatResponse createChat(String username, CreateChatRequest request) {
        User creator = getUser(username);

        Chat chat = Chat.builder()
                .type(request.getType())
                .name(request.getName())
                .createdBy(creator)
                .build();
        chatRepository.save(chat);

        ChatMember ownerMember = ChatMember.builder()
                .chat(chat)
                .user(creator)
                .role(ChatMemberRole.OWNER)
                .build();
        chatMemberRepository.save(ownerMember);

        if (request.getMemberIds() != null) {
            for (Long memberId : request.getMemberIds()) {
                User member = userRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + memberId));
                ChatMember chatMember = ChatMember.builder()
                        .chat(chat)
                        .user(member)
                        .role(ChatMemberRole.MEMBER)
                        .build();
                chatMemberRepository.save(chatMember);
            }
        }

        return toResponse(chat, username);
    }

    @Override
    public List<ChatResponse> getMyChats(String username) {
        User user = getUser(username);
        return chatRepository.findAllByMember(user)
                .stream()
                .map(chat -> toResponse(chat, username))
                .toList();
    }

    @Override
    public ChatResponse getChat(String username, Long chatId) {
        Chat chat = getChat(chatId);
        checkMember(chat, username);
        return toResponse(chat, username);
    }

    @Override
    @Transactional
    public ChatResponse updateChat(String username, Long chatId, UpdateChatRequest request) {
        Chat chat = getChat(chatId);
        checkAdmin(chat, username);
        if (request.getName() != null) chat.setName(request.getName());
        if (request.getAvatarUrl() != null) chat.setAvatarUrl(request.getAvatarUrl());
        chatRepository.save(chat);
        return toResponse(chat, username);
    }

    @Override
    @Transactional
    public void deleteOrLeaveChat(String username, Long chatId) {
        Chat chat = getChat(chatId);
        User user = getUser(username);
        ChatMember member = chatMemberRepository.findByChatAndUser(chat, user)
                .orElseThrow(() -> new RuntimeException("Вы не участник этого чата"));

        if (member.getRole() == ChatMemberRole.OWNER) {
            chatRepository.delete(chat);
        } else {
            chatMemberRepository.delete(member);
        }
    }

    @Override
    @Transactional
    public void addMember(String username, Long chatId, AddMemberRequest request) {
        Chat chat = getChat(chatId);
        checkAdmin(chat, username);
        User newMember = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        if (chatMemberRepository.existsByChatAndUser(chat, newMember)) {
            throw new RuntimeException("Пользователь уже в чате");
        }
        ChatMember chatMember = ChatMember.builder()
                .chat(chat)
                .user(newMember)
                .role(ChatMemberRole.MEMBER)
                .build();
        chatMemberRepository.save(chatMember);
    }

    @Override
    @Transactional
    public void removeMember(String username, Long chatId, Long userId) {
        Chat chat = getChat(chatId);
        checkAdmin(chat, username);
        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        chatMemberRepository.deleteByChatAndUser(chat, userToRemove);
    }

    @Override
    @Transactional
    public void updateMemberRole(String username, Long chatId, Long userId, UpdateMemberRoleRequest request) {
        Chat chat = getChat(chatId);
        checkOwner(chat, username);
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        ChatMember member = chatMemberRepository.findByChatAndUser(chat, targetUser)
                .orElseThrow(() -> new RuntimeException("Пользователь не в чате"));
        member.setRole(request.getRole());
        chatMemberRepository.save(member);
    }

    @Override
    public List<ChatMemberResponse> getMembers(String username, Long chatId) {
        Chat chat = getChat(chatId);
        checkMember(chat, username);
        return chatMemberRepository.findAllByChat(chat)
                .stream()
                .map(m -> ChatMemberResponse.builder()
                        .userId(m.getUser().getId())
                        .username(m.getUser().getUsername())
                        .role(m.getRole().name())
                        .joinedAt(m.getJoinedAt())
                        .build())
                .toList();
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

    private void checkAdmin(Chat chat, String username) {
        User user = getUser(username);
        ChatMember member = chatMemberRepository.findByChatAndUser(chat, user)
                .orElseThrow(() -> new RuntimeException("Нет доступа"));
        if (member.getRole() == ChatMemberRole.MEMBER) {
            throw new RuntimeException("Недостаточно прав");
        }
    }

    private void checkOwner(Chat chat, String username) {
        User user = getUser(username);
        ChatMember member = chatMemberRepository.findByChatAndUser(chat, user)
                .orElseThrow(() -> new RuntimeException("Нет доступа"));
        if (member.getRole() != ChatMemberRole.OWNER) {
            throw new RuntimeException("Только владелец может выполнить это действие");
        }
    }

    private ChatResponse toResponse(Chat chat, String username) {
        User user = getUser(username);
        long unread = messageRepository.countUnread(chat, user.getId());
        return ChatResponse.builder()
                .id(chat.getId())
                .type(chat.getType().name())
                .name(chat.getName())
                .avatarUrl(chat.getAvatarUrl())
                .createdAt(chat.getCreatedAt())
                .unreadCount(unread)
                .build();
    }
}