package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.persistence.entity.Image;
import com.victordev.ferreclickbackend.persistence.repository.ImageRepository;
import com.victordev.ferreclickbackend.service.ICloudinaryService;
import com.victordev.ferreclickbackend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ICloudinaryService cloudinaryService;
    @Autowired
    private ImageRepository imageRepository;


    @Override
    public String uploadImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            Image image = new Image();
            image.setUrl(cloudinaryService.uploadFile(file, "ferreclick"));
            String url = image.getUrl();
            if(url == null) {
                throw new RuntimeException("Error creating image url");
            }
            imageRepository.save(image);
            return url;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image");
        }
    }

    public void deleteImage(String imageUrl){
        try {
            cloudinaryService.deleteFile(imageUrl, "ferreclick");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting image", e);
        }
    }
}
