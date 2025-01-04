package com.example.FinderUnited.business.service.impl;

import com.example.FinderUnited.business.service.AuthenticationService;
import com.example.FinderUnited.business.service.AwsService;
import com.example.FinderUnited.business.service.FileService;
import com.example.FinderUnited.data.entities.File;
import com.example.FinderUnited.data.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final AuthenticationService authenticationService;
    private final AwsService awsService;

    @Override
    public File uploadFile(MultipartFile multipartFile) {
        String url = awsService.uploadImage(multipartFile);

        File file = File.builder()
                .userId(authenticationService.getAuthenticatedUser().getId())
                .fileUrl(url)
                .createdAt(System.currentTimeMillis())
                .build();
        return fileRepository.save(file);
    }
}
