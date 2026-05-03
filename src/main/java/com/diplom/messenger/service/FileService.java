package com.diplom.messenger.service;

import com.diplom.messenger.dto.response.AttachmentResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    AttachmentResponse uploadFile(String username, Long chatId, Long messageId, MultipartFile file);
    void downloadFile(String fileName, HttpServletResponse response);
}