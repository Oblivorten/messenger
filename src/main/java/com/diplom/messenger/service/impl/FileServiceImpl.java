package com.diplom.messenger.service.impl;

import com.diplom.messenger.dto.response.AttachmentResponse;
import com.diplom.messenger.entity.*;
import com.diplom.messenger.repository.*;
import com.diplom.messenger.service.FileService;
import com.diplom.messenger.service.MinioService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioService minioService;
    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;
    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "video/mp4", "audio/mpeg", "audio/ogg",
            "application/pdf", "text/plain",
            "application/zip"
    );

    @Override
    public AttachmentResponse uploadFile(String username, Long chatId, Long messageId, MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("Файл слишком большой. Максимум 50MB");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new RuntimeException("Недопустимый тип файла: " + file.getContentType());
        }

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Сообщение не найдено"));

        try {
            String fileUrl = minioService.upload(file);

            if (file.getContentType() != null && file.getContentType().startsWith("image/")) {
                uploadThumbnail(file, fileUrl);
            }

            Attachment attachment = Attachment.builder()
                    .message(message)
                    .fileUrl(fileUrl)
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .mimeType(file.getContentType())
                    .build();

            attachmentRepository.save(attachment);

            return AttachmentResponse.builder()
                    .id(attachment.getId())
                    .fileUrl(minioService.getUrl(fileUrl))
                    .fileName(attachment.getFileName())
                    .fileSize(attachment.getFileSize())
                    .mimeType(attachment.getMimeType())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки файла: " + e.getMessage());
        }
    }

    @Override
    public void downloadFile(String fileName, HttpServletResponse response) {
        try (var stream = minioService.download(fileName)) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            OutputStream out = response.getOutputStream();
            stream.transferTo(out);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка скачивания файла: " + e.getMessage());
        }
    }

    private void uploadThumbnail(MultipartFile file, String originalName) throws Exception {
        java.io.ByteArrayOutputStream thumbnailBytes = new java.io.ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(300, 300)
                .outputFormat("jpg")
                .toOutputStream(thumbnailBytes);

        minioService.uploadBytes(
                "thumbnail_" + originalName,
                thumbnailBytes.toByteArray(),
                "image/jpeg"
        );
    }
}