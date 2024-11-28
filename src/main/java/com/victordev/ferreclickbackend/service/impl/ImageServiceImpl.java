package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.ImageModel;
import com.victordev.ferreclickbackend.persistence.entity.Image;
import com.victordev.ferreclickbackend.persistence.repository.ImageRepository;
import com.victordev.ferreclickbackend.service.ICloudinaryService;
import com.victordev.ferreclickbackend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ICloudinaryService cloudinaryService;
    @Autowired
    private ImageRepository imageRepository;


    @Override
    public ResponseEntity<Map> uploadImage(ImageModel imageModel) {
        try {
            if (imageModel.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (imageModel.getFile().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Image image = new Image();
            image.setName(imageModel.getName());
            image.setUrl(cloudinaryService.uploadFile(imageModel.getFile(), "ferreclick"));
            if(image.getUrl() == null) {
                return ResponseEntity.badRequest().build();
            }
            imageRepository.save(image);
            return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
