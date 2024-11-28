package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageModel {
    private String name;
    private MultipartFile file;
}