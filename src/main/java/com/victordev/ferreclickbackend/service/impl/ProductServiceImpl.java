package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.product.ProductBody;
import com.victordev.ferreclickbackend.DTOs.api.product.ProductResponse;
import com.victordev.ferreclickbackend.DTOs.api.product.UpdateProductBody;
import com.victordev.ferreclickbackend.exceptions.product.FailedProductCreationException;
import com.victordev.ferreclickbackend.exceptions.product.ProductAlreadyExistsException;
import com.victordev.ferreclickbackend.exceptions.product.ProductNotExistException;
import com.victordev.ferreclickbackend.exceptions.productCategories.AddCategoriesToProductException;
import com.victordev.ferreclickbackend.exceptions.productCategories.CategoryNotFoundException;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import com.victordev.ferreclickbackend.persistence.repository.*;
import com.victordev.ferreclickbackend.service.IProductService;
import com.victordev.ferreclickbackend.service.ImageService;
import com.victordev.ferreclickbackend.utils.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de productos que proporciona métodos para crear, obtener, actualizar y eliminar productos.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    /**
     * Repositorio de productos.
     */
    private final ProductRepository productRepository;
    /**
     * Repositorio de categorías de productos.
     */
    private final ProductCategoryRepository categoryRepository;
    /**
     * Conversor de DTO.
     */
    private final DtoConverter dtoConverter;
    /**
     * Repositorio de categorías de productos.
     */
    private final ImageService imageService;


    /**
     * Crea un nuevo producto.
     * @param productBody Objeto que contiene la información del producto a crear.
     * @return Objeto que contiene la información del producto creado.
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse createProduct(ProductBody productBody) {

        Optional<Product> productVerification = productRepository.findByNameIgnoreCase(productBody.getName());
        if(productVerification.isPresent()){
            throw new ProductAlreadyExistsException("A product already exists with the name: " + productBody.getName());
        }

        String imageUrl = imageService.uploadImage(productBody.getImage());

        try{
            Product newProduct = new Product(productBody.getName(), productBody.getDescription(), productBody.getPrice(), imageUrl);

            Product savedProduct = productRepository.save(newProduct);

            addCategoriesToProduct(productBody.getCategoryIds(), savedProduct);

            return dtoConverter.getProduct(savedProduct);
        } catch (DataIntegrityViolationException e) {
            throw new FailedProductCreationException("Data Integrity Error During Product Creation", e);
        } catch (Exception e) {
            throw new FailedProductCreationException("Unexpected Error During Product Creation", e);
        }
    }

    /**
     * Obtiene todos los productos.
     * @return Lista de objetos `ProductResponse` que representan los productos.
     */
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(dtoConverter::getProduct).collect(Collectors.toList());
    }

    /**
     * Obtiene un producto por su identificador.
     * @param id Identificador del producto.
     * @return Objeto `ProductResponse` que representa el producto.
     */
    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id).map(dtoConverter::getProduct);
    }

    /**
     * Obtiene los productos de una categoría.
     * @param categoryId Identificador de la categoría.
     * @return Lista de objetos `ProductResponse` que representan los productos de la categoría.
     */
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        Optional<ProductCategory> category = categoryRepository.findById(categoryId);
        return category.map(productCategory -> productCategory.getProducts().stream()
                .map(product_productCategory -> dtoConverter.getProduct(product_productCategory.getProduct()))
                .toList()).orElseGet(ArrayList::new);

    }

    /**
     * Actualiza un producto.
     * @param updateProductBody Objeto que contiene la información del producto a actualizar.
     * @return Objeto que contiene la información del producto actualizado.
     */
    @Transactional
    public ProductResponse updateProduct(UpdateProductBody updateProductBody) {

        Optional<Product> existingProduct = productRepository.findById(updateProductBody.getId());

        if(existingProduct.isEmpty()){
            throw new ProductNotExistException("Product not found with id: " + updateProductBody.getId());
        }

        existingProduct.get().setName(updateProductBody.getName());
        existingProduct.get().setDescription(updateProductBody.getDescription());
        existingProduct.get().setPrice(updateProductBody.getPrice());

        if(updateProductBody.getImage() != null && !updateProductBody.getImage().isEmpty()){
            imageService.deleteImage(existingProduct.get().getImage());
            existingProduct.get().setImage(imageService.uploadImage(updateProductBody.getImage()));
        }

        if(updateProductBody.getCategoryIds() != null && !updateProductBody.getCategoryIds().isEmpty()) {
            addCategoriesToProduct(updateProductBody.getCategoryIds(), existingProduct.get());
        }

        return dtoConverter.getProduct(existingProduct.get());
    }

    /**
     * Elimina un producto.
     * @param productId Identificador del producto a eliminar.
     */
    @Transactional
    public void deleteProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        try {
            productRepository.deleteById(product.getId());
            imageService.deleteImage(product.getImage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete product with id: " + productId, e);
        }
    }

    /**
     * Añade categorías a un producto.
     * @param categoryIds Identificadores de las categorías a añadir.
     * @param product Producto al que se añadirán las categorías.
     */
    @Transactional
    protected void addCategoriesToProduct(List<Long> categoryIds, Product product){
        try {
            List<Product_ProductCategory> productCategories = categoryIds.stream()
                    .map(categoryId -> {
                        ProductCategory category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
                        return new Product_ProductCategory(product, category);
                    })
                    .toList();

            product.getCategories().clear();
            product.getCategories().addAll(productCategories);
        }catch (Exception e){
            throw new AddCategoriesToProductException("Error adding categories to product", e);
        }
    }
}