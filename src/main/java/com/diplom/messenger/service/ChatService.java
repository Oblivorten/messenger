package com.diplom.messenger.service;

import com.diplom.messenger.dto.request.*;
import com.diplom.messenger.dto.response.ChatMemberResponse;
import com.diplom.messenger.dto.response.ChatResponse;

import java.util.List;

public interface ChatService {
    ChatResponse createChat(String username, CreateChatRequest request);
    List<ChatResponse> getMyChats(String username);
    ChatResponse getChat(String username, Long chatId);
    ChatResponse updateChat(String username, Long chatId, UpdateChatRequest request);
    void deleteOrLeaveChat(String username, Long chatId);
    void addMember(String username, Long chatId, AddMemberRequest request);
    void removeMember(String username, Long chatId, Long userId);
    void updateMemberRole(String username, Long chatId, Long userId, UpdateMemberRoleRequest request);
    List<ChatMemberResponse> getMembers(String username, Long chatId);
    void muteChat(String username, Long chatId, MuteChatRequest request);
}