package com.diplom.messenger.controller;

import com.diplom.messenger.dto.request.InitiateCallRequest;
import com.diplom.messenger.dto.response.CallHistoryResponse;
import com.diplom.messenger.dto.response.CallResponse;
import com.diplom.messenger.service.CallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;

    @PostMapping("/initiate")
    public ResponseEntity<CallResponse> initiateCall(@AuthenticationPrincipal UserDetails userDetails,
                                                     @Valid @RequestBody InitiateCallRequest request) {
        return ResponseEntity.ok(callService.initiateCall(userDetails.getUsername(), request));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<CallResponse> acceptCall(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok(callService.acceptCall(userDetails.getUsername(), id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectCall(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable Long id) {
        callService.rejectCall(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<Void> endCall(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable Long id) {
        callService.endCall(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<CallHistoryResponse>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(callService.getHistory(userDetails.getUsername()));
    }
}