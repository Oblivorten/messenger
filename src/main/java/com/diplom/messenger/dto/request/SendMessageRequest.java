package com.diplom.messenger.dto.request;

import com.diplom.messenger.entity.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMessageRequest {
    @NotNull
    private MessageType type;
    private String encryptedContent;
    private String ivHex;
    private Long replyToId;
}