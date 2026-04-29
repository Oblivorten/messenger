package com.diplom.messenger.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WebSocketEvent {
    private String type;
    private Object payload;
}