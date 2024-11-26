package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.ProductCategoryResponse;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import com.victordev.ferreclickbackend.persistence.repository.ProductCategoryRepository;
import com.victordev.ferreclickbackend.persistence.repository.ProductRepository;
import com.victordev.ferreclickbackend.persistence.repository.Product_ProductCategoryRepository;
import com.victordev.ferreclickbackend.service.IProductCategoryService;
import com.victordev.ferreclickbackend.utils.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Product_ProductCategoryRepository productProductCategoryRepository;

    @Autowired
    private DtoConverter dtoConverter;

    @Transactional
    public ProductCategoryResponse createCategory(ProductCategory productCategory, List<Long> productIds) {
        ProductCategory savedCategory = categoryRepository.save(productCategory);

        List<Product_ProductCategory> products;
        if (productIds != null && !productIds.isEmpty()) {
            products = productIds.stream()
                    .map(productId -> {
                        Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("product not found with id " + productId));
                        return new Product_ProductCategory(product, savedCategory);
                    })
                    .collect(Collectors.toList());

            productProductCategoryRepository.saveAll(products);
            savedCategory.setProducts(products);
        }
        return dtoConverter.getProductCategory(savedCategory);
    }

    public List<ProductCategoryResponse> getCategories() {
        return categoryRepository.findAll().stream().map(dtoConverter::getProductCategory).collect(Collectors.toList());
    }

    public Optional<ProductCategoryResponse> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(dtoConverter::getProductCategory);
    }
}