package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.dto.api.ProductCategoryBody;
import com.victordev.ferreclickbackend.dto.api.ProductCategoryResponse;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.service.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    @Autowired
    private IProductCategoryService productCategoryService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<ProductCategoryResponse> getAllProductCategories() {
        return productCategoryService.getCategories();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResponse> getProductCategoryById(@PathVariable Long id) {
        return productCategoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('category:create')")
    @PostMapping
    public ResponseEntity<ProductCategoryResponse> createProductCategory(@RequestBody ProductCategoryBody productCategoryBody) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(productCategoryBody.getName());
        productCategory.setDescription(productCategoryBody.getDescription());

        ProductCategoryResponse productCategoryResponse = productCategoryService.createCategory(productCategory, productCategoryBody.getProductIds());
        return ResponseEntity.ok(productCategoryResponse);
    }
}
