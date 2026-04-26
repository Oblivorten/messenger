package com.diplom.messenger.controller;

import com.diplom.messenger.dto.request.SendMessageRequest;
import com.diplom.messenger.dto.response.MessageResponse;
import com.diplom.messenger.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long id) {
        messageService.deleteMessage(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> editMessage(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable Long id,
                                                       @RequestBody SendMessageRequest request) {
        return ResponseEntity.ok(messageService.editMessage(userDetails.getUsername(), id, request));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable Long id) {
        messageService.markAsRead(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }
}