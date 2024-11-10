package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductBody {
    private String name;
    private String description;
    private String image;
    private Double price;
    private List<Long> categoryIds;
}
