package com.diplom.messenger.controller;

import com.diplom.messenger.dto.request.*;
import com.diplom.messenger.dto.response.*;
import com.diplom.messenger.service.ChatService;
import com.diplom.messenger.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ChatResponse> createChat(@AuthenticationPrincipal UserDetails userDetails,
                                                   @Valid @RequestBody CreateChatRequest request) {
        return ResponseEntity.ok(chatService.createChat(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getMyChats(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(chatService.getMyChats(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> getChat(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(chatService.getChat(userDetails.getUsername(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatResponse> updateChat(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable Long id,
                                                   @RequestBody UpdateChatRequest request) {
        return ResponseEntity.ok(chatService.updateChat(userDetails.getUsername(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrLeaveChat(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Long id) {
        chatService.deleteOrLeaveChat(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMember(@AuthenticationPrincipal UserDetails userDetails,
                                          @PathVariable Long id,
                                          @Valid @RequestBody AddMemberRequest request) {
        chatService.addMember(userDetails.getUsername(), id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeMember(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long id,
                                             @PathVariable Long userId) {
        chatService.removeMember(userDetails.getUsername(), id, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> updateMemberRole(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id,
                                                 @PathVariable Long userId,
                                                 @Valid @RequestBody UpdateMemberRoleRequest request) {
        chatService.updateMemberRole(userDetails.getUsername(), id, userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<ChatMemberResponse>> getMembers(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable Long id) {
        return ResponseEntity.ok(chatService.getMembers(userDetails.getUsername(), id));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@AuthenticationPrincipal UserDetails userDetails,
                                                             @PathVariable Long id,
                                                             @RequestParam(required = false) Long cursor,
                                                             @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(messageService.getMessages(userDetails.getUsername(), id, cursor, limit));
    }

    @PutMapping("/{id}/mute")
    public ResponseEntity<Void> muteChat(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable Long id,
                                         @RequestBody MuteChatRequest request) {
        chatService.muteChat(userDetails.getUsername(), id, request);
        return ResponseEntity.ok().build();
    }
}