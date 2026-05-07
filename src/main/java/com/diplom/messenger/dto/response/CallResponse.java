package com.diplom.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CallResponse {
    private Long callId;
    private String livekitToken;
    private String livekitUrl;
    private String type;
    private String status;
    private LocalDateTime startedAt;
}