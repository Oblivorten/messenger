package com.diplom.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private String mimeType;
}