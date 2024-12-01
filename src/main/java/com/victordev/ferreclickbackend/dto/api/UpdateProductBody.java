package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UpdateProductBody {
    private Long id;
    private String name;
    private String description;
    private MultipartFile image;
    private Double price;
    private List<Long> categoryIds;
}
