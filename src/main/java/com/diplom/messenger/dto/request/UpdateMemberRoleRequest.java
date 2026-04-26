package com.diplom.messenger.dto.request;

import com.diplom.messenger.entity.ChatMemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMemberRoleRequest {
    @NotNull
    private ChatMemberRole role;
}