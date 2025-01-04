package com.example.FinderUnited.business.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.FinderUnited.business.service.AwsService;
import com.example.FinderUnited.business.service.exceptions.UserInfoException;
import com.example.FinderUnited.data.repositories.FileRepository;
import com.example.FinderUnited.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsServiceImpl implements AwsService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null) {
            throw new UserInfoException("File is empty!");
        }

        // file.type
        String actualFileName = file.getOriginalFilename();

        // xyzfile.type
        String newFileName = UUID.randomUUID() + actualFileName.substring(actualFileName.lastIndexOf("."));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        try {
            amazonS3Client.putObject(new PutObjectRequest(
                    bucketName,
                    newFileName,
                    file.getInputStream(),
                    metadata
            ));
            return preSignedUrl(newFileName);
        } catch (IOException e) {
            throw new UserInfoException("Something went wrong to uplaod the upload the image.");
        }
    }

    @Override
    public String preSignedUrl(String fileName) {
        Date expirationDate = new Date();
        int hour = 2;
        long time = expirationDate.getTime();
        time = time + 60 * 60 * 1000;
        expirationDate.setTime(time);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
                bucketName,
                fileName
        );
        URL url = amazonS3Client.generatePresignedUrl(request);
        return url.toString();
    }
}
