package com.example.FinderUnited.controller;

import com.example.FinderUnited.business.dto.DefaultResponseDto;
import com.example.FinderUnited.business.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.example.FinderUnited.business.dto.DefaultResponseDto.Status.SUCCESS;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<DefaultResponseDto> uploadFile(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("data", fileService.uploadFile(file)),
                        "File uplaoded successfully"
                )
        );
    }
}