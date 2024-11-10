package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.ProductCategoryResponse;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface IProductCategoryService {
    ProductCategoryResponse createCategory(ProductCategory productCategory, List<Long> productIds);

    List<ProductCategoryResponse> getCategories();

    Optional<ProductCategoryResponse> getCategoryById(Long id);
}
