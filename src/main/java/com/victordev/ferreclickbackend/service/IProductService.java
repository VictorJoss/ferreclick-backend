package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.ProductResponse;
import com.victordev.ferreclickbackend.persistence.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductResponse createProduct(Product product, List<Long> categoryIds);

    List<ProductResponse> getAllProducts();

    Optional<ProductResponse> getProductById(Long id);
    List<ProductResponse> getProductsByCategory(Long categoryId);
}