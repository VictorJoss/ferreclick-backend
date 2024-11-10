package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private List<Long> productIds;
}
