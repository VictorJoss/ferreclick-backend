package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.ImageModel;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ImageService {
    ResponseEntity<Map> uploadImage(ImageModel imageModel);
}
