package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.ProductResponse;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import com.victordev.ferreclickbackend.persistence.repository.ProductCategoryRepository;
import com.victordev.ferreclickbackend.persistence.repository.ProductRepository;
import com.victordev.ferreclickbackend.persistence.repository.Product_ProductCategoryRepository;
import com.victordev.ferreclickbackend.service.IProductService;
import com.victordev.ferreclickbackend.utils.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private Product_ProductCategoryRepository productProductCategoryRepository;

    @Autowired
    private DtoConverter dtoConverter;

    @Transactional
    public ProductResponse createProduct(Product product, List<Long> categoryIds) {
        Product savedProduct = productRepository.save(product);

        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<Product_ProductCategory> productCategories = categoryIds.stream()
                    .map(categoryId -> {
                        ProductCategory category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
                        return new Product_ProductCategory(savedProduct, category);
                    })
                    .collect(Collectors.toList());

            productProductCategoryRepository.saveAll(productCategories);
            savedProduct.setCategories(productCategories);
        }
        return dtoConverter.getProduct(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(dtoConverter::getProduct).collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id).map(dtoConverter::getProduct);
    }
}