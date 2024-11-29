package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.ProductBody;
import com.victordev.ferreclickbackend.dto.api.ProductResponse;
import com.victordev.ferreclickbackend.persistence.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductResponse createProduct(ProductBody productBody);
    List<ProductResponse> getAllProducts();
    Optional<ProductResponse> getProductById(Long id);
    List<ProductResponse> getProductsByCategory(Long categoryId);
}