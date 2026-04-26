package com.diplom.messenger.dto.request;

import com.diplom.messenger.entity.ChatType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateChatRequest {
    @NotNull
    private ChatType type;
    private String name;
    private List<Long> memberIds;
}