package com.victordev.ferreclickbackend.utils;

import com.victordev.ferreclickbackend.dto.api.ProductCategoryBody;
import com.victordev.ferreclickbackend.dto.api.ProductResponse;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class DtoConverter {

    public ProductResponse getProduct(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setImage(product.getImage());
        productResponse.setPrice(product.getPrice());
        productResponse.setCategoryIds(product.getCategories().stream()
                .map(pc -> pc.getCategory().getId())
                .collect(Collectors.toList()));
        return productResponse;
    }

    public ProductCategoryBody getProductCategory(ProductCategory category) {
        ProductCategoryBody productCategoryBody = new ProductCategoryBody();
        productCategoryBody.setId(category.getId());
        productCategoryBody.setName(category.getName());
        productCategoryBody.setDescription(category.getDescription());
        if (category.getProducts() == null || category.getProducts().isEmpty()) {
            productCategoryBody.setProductIds(new ArrayList<>());
            return productCategoryBody;
        }
        productCategoryBody.setProductIds(category.getProducts().stream()
                    .map(pc -> pc.getProduct().getId())
                    .collect(Collectors.toList()));
        return productCategoryBody;
    }
}