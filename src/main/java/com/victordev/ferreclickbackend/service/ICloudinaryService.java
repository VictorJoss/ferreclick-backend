package com.victordev.ferreclickbackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {
    String uploadFile(MultipartFile file, String folderName);
    void deleteFile(String publicId, String folderName);
}
