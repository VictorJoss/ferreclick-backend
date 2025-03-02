package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.productCategory.ProductCategoryBody;
import com.victordev.ferreclickbackend.exceptions.productCategories.CategoryAlreadyExistException;
import com.victordev.ferreclickbackend.exceptions.productCategories.CategoryNotFoundException;
import com.victordev.ferreclickbackend.exceptions.productCategories.CategoryProductException;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import com.victordev.ferreclickbackend.persistence.repository.ProductCategoryRepository;
import com.victordev.ferreclickbackend.service.IEntityFinderService;
import com.victordev.ferreclickbackend.service.IProductCategoryService;
import com.victordev.ferreclickbackend.utils.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
     * Conversor de DTO.
     */
    private final DtoConverter dtoConverter;
    /**
     * Servicio de búsqueda de entidades.
     */
    private final IEntityFinderService entityFinderService;

    /**
     * Crea una nueva categoría de productos.
     * @param categoryBody Objeto que contiene la información de la categoría a crear.
     * @return Objeto que contiene la información de la categoría creada.
     */
    @Transactional
    public ProductCategoryBody createCategory(ProductCategoryBody categoryBody) {

        categoryRepository.findByNameIgnoreCase(categoryBody.getName())
                .ifPresent(category -> {
                    throw new CategoryAlreadyExistException("A category already exists with the name: " + categoryBody.getName());
                });

            ProductCategory newCategory = new ProductCategory();
            newCategory.setName(categoryBody.getName());
            newCategory.setDescription(categoryBody.getDescription());
            newCategory.setProducts(new ArrayList<>());

            ProductCategory savedCategory = categoryRepository.save(newCategory);
            addProductsToCategory(categoryBody.getProductIds(), savedCategory);

            return dtoConverter.getProductCategory(savedCategory);
    }

    /**
     * Obtiene todas las categorías de productos.
     * @return Lista de objetos `ProductCategoryBody` que representan las categorías de productos.
     */
public List<ProductCategoryBody> getCategories() {
    List<ProductCategoryBody> categories = categoryRepository.findAll().stream()
            .map(dtoConverter::getProductCategory)
            .toList();

    if (categories.isEmpty()) {
        throw new CategoryNotFoundException("No categories found");
    }
    return categories;
}

    public ProductCategoryBody getCategoryById(Long id) {
        return dtoConverter.getProductCategory(entityFinderService.getCategoryById(id));
        }

    /**
     * Actualiza una categoría de productos.
     * @param categoryBody Objeto que contiene la información de la categoría a actualizar.
     * @return Objeto que contiene la información de la categoría actualizada.
     */
    @Transactional
    public ProductCategoryBody updateCategory(ProductCategoryBody categoryBody) {

        ProductCategory existingCategory = entityFinderService.getCategoryById(categoryBody.getId());

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

        ProductCategory categoryFound = entityFinderService.getCategoryById(categoryId);
        categoryRepository.deleteById(categoryFound.getId());
    }

    /**
     * Añade productos a una categoría.
     * @param productIds Identificadores de los productos a añadir.
     * @param category Categoría a la que se añadirán los productos.
     */
    @Transactional
    protected void addProductsToCategory(List<Long> productIds, ProductCategory category){

        category.getProducts().clear();
        if (productIds != null && !productIds.isEmpty()) {
            List<Product_ProductCategory> products = productIds.stream()
                    .map(entityFinderService::getProductById)
                    .map(product -> new Product_ProductCategory(product, category))
                    .toList();
            category.getProducts().addAll(products);
        }
    }
}