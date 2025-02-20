package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.productCategory.ProductCategoryBody;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import com.victordev.ferreclickbackend.persistence.repository.ProductCategoryRepository;
import com.victordev.ferreclickbackend.persistence.repository.ProductRepository;
import com.victordev.ferreclickbackend.service.IProductCategoryService;
import com.victordev.ferreclickbackend.utils.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de categorías de productos que proporciona métodos para crear, obtener, actualizar y
 * eliminar categorías de productos.
 */
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements IProductCategoryService {

    /**
     * Repositorio de categorías de productos.
     */
    private final ProductCategoryRepository categoryRepository;
    /**
     * Repositorio de productos.
     */
    private final ProductRepository productRepository;
    /**
     * Conversor de DTO.
     */
    private final DtoConverter dtoConverter;

    /**
     * Crea una nueva categoría de productos.
     * @param categoryBody Objeto que contiene la información de la categoría a crear.
     * @return Objeto que contiene la información de la categoría creada.
     */
    @Transactional
    public ProductCategoryBody createCategory(ProductCategoryBody categoryBody) {

        Optional<ProductCategory> categoryVerification = categoryRepository.findByNameIgnoreCase(categoryBody.getName());
        if(categoryVerification.isPresent()){
            throw new RuntimeException("A category already exist with the name: " + categoryBody.getName());
        }

        try{
            ProductCategory newCategory = new ProductCategory();
            newCategory.setName(categoryBody.getName());
            newCategory.setDescription(categoryBody.getDescription());
            newCategory.setProducts(new ArrayList<>());

            ProductCategory savedCategory = categoryRepository.save(newCategory);

            addProductsToCategory(categoryBody.getProductIds(), savedCategory);

            return dtoConverter.getProductCategory(savedCategory);
        }catch (Exception e){
            throw new RuntimeException("Failed to create category", e);
        }
    }

    /**
     * Obtiene todas las categorías de productos.
     * @return Lista de objetos `ProductCategoryBody` que representan las categorías de productos.
     */
    public List<ProductCategoryBody> getCategories() {
        return categoryRepository.findAll().stream().map(dtoConverter::getProductCategory).collect(Collectors.toList());
    }

    public Optional<ProductCategoryBody> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(dtoConverter::getProductCategory);
    }

    /**
     * Actualiza una categoría de productos.
     * @param categoryBody Objeto que contiene la información de la categoría a actualizar.
     * @return Objeto que contiene la información de la categoría actualizada.
     */
    @Transactional
    public ProductCategoryBody updateCategory(ProductCategoryBody categoryBody) {

        ProductCategory existingCategory = categoryRepository.findById(categoryBody.getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryBody.getId()));

        existingCategory.setName(categoryBody.getName());
        existingCategory.setDescription(categoryBody.getDescription());

        addProductsToCategory(categoryBody.getProductIds(), existingCategory);
        return dtoConverter.getProductCategory(existingCategory);
    }

    /**
     * Elimina una categoría de productos.
     * @param categoryId Identificador de la categoría a eliminar.
     */
    @Transactional
    public void deleteCategory(Long categoryId){
        ProductCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + categoryId));
        try {
            categoryRepository.deleteById(category.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete category with id: " + categoryId, e);
        }
    }

    /**
     * Añade productos a una categoría.
     * @param productIds Identificadores de los productos a añadir.
     * @param category Categoría a la que se añadirán los productos.
     */
    @Transactional
    protected void addProductsToCategory(List<Long> productIds, ProductCategory category){

        if (productIds != null && !productIds.isEmpty()) {
            List<Product_ProductCategory> products = productIds.stream()
                    .map(productId -> {
                        Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("product not found with id " + productId));
                        return new Product_ProductCategory(product, category);
                    })
                    .toList();

            category.getProducts().clear();
            category.getProducts().addAll(products);
        }
        category.getProducts().clear();
    }
}