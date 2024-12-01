package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DProductCategoryBody {
    private String name;
    private String description;
    private List<Long> productIds;
}
