package com.diplom.messenger.controller;

import com.diplom.messenger.dto.response.AttachmentResponse;
import com.diplom.messenger.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> upload(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long chatId,
            @RequestParam Long messageId,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(
                fileService.uploadFile(userDetails.getUsername(), chatId, messageId, file));
    }

    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        fileService.downloadFile(fileName, response);
    }
}