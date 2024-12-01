package com.victordev.ferreclickbackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile file);
    void deleteImage(String imageUrl);
}
