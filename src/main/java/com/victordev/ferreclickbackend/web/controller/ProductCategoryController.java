package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.dto.api.ProductCategoryBody;
import com.victordev.ferreclickbackend.service.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    @Autowired
    private IProductCategoryService productCategoryService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<ProductCategoryBody> getAllProductCategories() {
        return productCategoryService.getCategories();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryBody> getProductCategoryById(@PathVariable Long id) {
        return productCategoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('category:create')")
    @PostMapping
    public ResponseEntity<ProductCategoryBody> createProductCategory(@RequestBody ProductCategoryBody productCategoryBody) {
        ProductCategoryBody createdCategory = productCategoryService.createCategory(productCategoryBody);
        URI location = URI.create("/api/product-categories" + createdCategory.getId());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAuthority('category:update')")
    @PutMapping
    public ResponseEntity<ProductCategoryBody> updateCategory(@RequestBody ProductCategoryBody categoryBody){
        return ResponseEntity.ok(productCategoryService.updateCategory(categoryBody));
    }

    @PreAuthorize("hasAuthority('category:delete')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId){
        productCategoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
