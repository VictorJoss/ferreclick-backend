package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.ProductBody;
import com.victordev.ferreclickbackend.dto.api.ProductResponse;
import com.victordev.ferreclickbackend.dto.api.UpdateProductBody;
import com.victordev.ferreclickbackend.exceptions.product.ProductCreationException;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import com.victordev.ferreclickbackend.persistence.repository.*;
import com.victordev.ferreclickbackend.service.IProductService;
import com.victordev.ferreclickbackend.service.ImageService;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public ProductResponse createProduct(ProductBody productBody) {

        Optional<Product> productVerification = productRepository.findByNameIgnoreCase(productBody.getName());
        if(productVerification.isPresent()){
            throw new RuntimeException("A product already exist with the name: " + productBody.getName());
        }

        if (productBody.getImage().isEmpty() || !productBody.getImage().getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen válida.");
        }

        try{
            Product newProduct = new Product();
            newProduct.setName(productBody.getName());
            newProduct.setDescription(productBody.getDescription());
            newProduct.setPrice(productBody.getPrice());
            newProduct.setImage(
                    imageService.uploadImage(productBody.getImage())
            );

            Product savedProduct = productRepository.save(newProduct);

            List<Long> categoryIds = productBody.getCategoryIds();

            addCategoriesToProduct(categoryIds, savedProduct);

            return dtoConverter.getProduct(savedProduct);
        } catch (Exception e) {
            throw new ProductCreationException("Failed to create product", e);
        }
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(dtoConverter::getProduct).collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id).map(dtoConverter::getProduct);
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        List<Product> products = productProductCategoryRepository.findProductsByCategoryId(categoryId);
        return products.stream()
                .map(dtoConverter::getProduct)
                .collect(Collectors.toList());
    }

    @Transactional
    protected void addCategoriesToProduct(List<Long> categoryIds, Product product){
        if (categoryIds == null && categoryIds.isEmpty()) {
            throw new RuntimeException("Categories is null or empty");
        }
            List<Product_ProductCategory> productCategories = categoryIds.stream()
                    .map(categoryId -> {
                        ProductCategory category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
                        return new Product_ProductCategory(product, category);
                    })
                    .collect(Collectors.toList());

            productProductCategoryRepository.saveAll(productCategories);
            product.setCategories(productCategories);
    }
}