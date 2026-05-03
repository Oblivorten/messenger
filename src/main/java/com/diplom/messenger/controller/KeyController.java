package com.diplom.messenger.controller;

import com.diplom.messenger.dto.request.RegisterKeyRequest;
import com.diplom.messenger.dto.request.SaveChatKeyRequest;
import com.diplom.messenger.dto.response.ChatKeyResponse;
import com.diplom.messenger.dto.response.KeyPairResponse;
import com.diplom.messenger.dto.response.PublicKeyResponse;
import com.diplom.messenger.service.KeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keys")
@RequiredArgsConstructor
public class KeyController {

    private final KeyService keyService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerKeyPair(@AuthenticationPrincipal UserDetails userDetails,
                                                @Valid @RequestBody RegisterKeyRequest request) {
        keyService.registerKeyPair(userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<KeyPairResponse> getMyKeyPair(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(keyService.getMyKeyPair(userDetails.getUsername()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PublicKeyResponse> getPublicKey(@PathVariable Long userId) {
        return ResponseEntity.ok(keyService.getPublicKey(userId));
    }

    @PostMapping("/chats/{chatId}")
    public ResponseEntity<Void> saveChatKey(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Long chatId,
                                            @Valid @RequestBody SaveChatKeyRequest request) {
        keyService.saveChatKey(userDetails.getUsername(), chatId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chats/{chatId}")
    public ResponseEntity<ChatKeyResponse> getChatKey(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable Long chatId) {
        return ResponseEntity.ok(keyService.getChatKey(userDetails.getUsername(), chatId));
    }
}