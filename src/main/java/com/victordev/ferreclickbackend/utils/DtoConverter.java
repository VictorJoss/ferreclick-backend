package com.victordev.ferreclickbackend.utils;

import com.victordev.ferreclickbackend.DTOs.api.productCategory.ProductCategoryBody;
import com.victordev.ferreclickbackend.DTOs.api.product.ProductResponse;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Clase que proporciona métodos para convertir entidades en objetos DTO.
 */
@Component
public class DtoConverter {

    /**
     * Convierte un objeto `Product` en un objeto `ProductResponse`.
     * @param product Objeto `Product` a convertir.
     * @return Objeto `ProductResponse` convertido.
     */
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

    /**
     * Convierte un objeto `ProductCategory` en un objeto `ProductCategoryBody`.
     * @param category Objeto `ProductCategory` a convertir.
     * @return Objeto `ProductCategoryBody` convertido.
     */
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