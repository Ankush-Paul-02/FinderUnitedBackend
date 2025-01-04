package com.example.FinderUnited.business.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsService {
    String uploadImage(MultipartFile file);

    String preSignedUrl(String fileName);
}
