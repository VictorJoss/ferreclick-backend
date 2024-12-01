package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.ProductCategoryBody;
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

import java.util.ArrayList;
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
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Transactional
    public ProductCategoryBody createCategory(ProductCategoryBody categoryBody) {

        Optional<ProductCategory> categoryVerification = productCategoryRepository.findByNameIgnoreCase(categoryBody.getName());
        if(categoryVerification.isPresent()){
            throw new RuntimeException("A category already exist with the name: " + categoryBody.getName());
        }

        try{
            ProductCategory newCategory = new ProductCategory();
            newCategory.setName(categoryBody.getName());
            newCategory.setDescription(categoryBody.getDescription());

            ProductCategory savedCategory = categoryRepository.save(newCategory);

            List<Long> productIds = categoryBody.getProductIds();

            addProductsToCategory(productIds, savedCategory);

            return dtoConverter.getProductCategory(savedCategory);
        }catch (Exception e){
            throw new RuntimeException("Failed to create category", e);
        }
    }

    public List<ProductCategoryBody> getCategories() {
        return categoryRepository.findAll().stream().map(dtoConverter::getProductCategory).collect(Collectors.toList());
    }

    public Optional<ProductCategoryBody> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(dtoConverter::getProductCategory);
    }

    @Transactional
    public ProductCategoryBody updateCategory(ProductCategoryBody categoryBody) {

        ProductCategory existingCategory = productCategoryRepository.findById(categoryBody.getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryBody.getId()));

        existingCategory.setName(categoryBody.getName());
        existingCategory.setDescription(categoryBody.getDescription());
        existingCategory.setProducts(new ArrayList<>());
        productProductCategoryRepository.deleteByCategory_Id(existingCategory.getId());

        List<Long> productIds = categoryBody.getProductIds();
        addProductsToCategory(productIds, existingCategory);

        ProductCategory savedCategory = productCategoryRepository.save(existingCategory);
        return dtoConverter.getProductCategory(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long categoryId){
        ProductCategory category = productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + categoryId));
        try {
            productCategoryRepository.deleteById(category.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete category with id: " + categoryId, e);
        }
    }

    @Transactional
    protected void addProductsToCategory(List<Long> productIds, ProductCategory category){

        if (productIds != null && !productIds.isEmpty()) {
            List<Product_ProductCategory> products = productIds.stream()
                    .map(productId -> {
                        Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("product not found with id " + productId));
                        return new Product_ProductCategory(product, category);
                    })
                    .collect(Collectors.toList());

            productProductCategoryRepository.saveAll(products);
            category.setProducts(products);
        }
    }
}