package com.diplom.messenger.dto.request;

import com.diplom.messenger.entity.CallType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InitiateCallRequest {
    @NotNull
    private Long chatId;
    @NotNull
    private CallType type;
}