package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.ProductCategoryBody;

import java.util.List;
import java.util.Optional;

public interface IProductCategoryService {
    ProductCategoryBody createCategory(ProductCategoryBody categoryBody);

    List<ProductCategoryBody> getCategories();

    Optional<ProductCategoryBody> getCategoryById(Long id);

    ProductCategoryBody updateCategory(ProductCategoryBody categoryBody);

    void deleteCategory(Long categoryId);
}
