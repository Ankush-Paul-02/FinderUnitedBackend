package com.example.FinderUnited.business.service;

import com.example.FinderUnited.data.entities.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    File uploadFile(MultipartFile multipartFile);
}
