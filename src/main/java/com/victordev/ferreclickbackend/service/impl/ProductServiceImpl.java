package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.product.ProductBody;
import com.victordev.ferreclickbackend.DTOs.api.product.ProductResponse;
import com.victordev.ferreclickbackend.DTOs.api.product.UpdateProductBody;
import com.victordev.ferreclickbackend.exceptions.product.FailedProductCreationException;
import com.victordev.ferreclickbackend.exceptions.product.ProductAlreadyExistsException;
import com.victordev.ferreclickbackend.exceptions.product.ProductException;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import com.victordev.ferreclickbackend.persistence.repository.*;
import com.victordev.ferreclickbackend.service.IEntityFinderService;
import com.victordev.ferreclickbackend.service.IProductService;
import com.victordev.ferreclickbackend.service.ImageService;
import com.victordev.ferreclickbackend.utils.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
     * Servicio de búsqueda de entidades.
     */
    private final IEntityFinderService entityFinderService;
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

        productRepository.findByNameIgnoreCase(productBody.getName())
                .ifPresent(product -> {
                    throw new ProductAlreadyExistsException("A product already exists with the name: " + productBody.getName());
                });

        Product newProduct = new Product(productBody.getName(), productBody.getDescription(), productBody.getPrice());
        newProduct.setCategories(new ArrayList<>());

        addCategoriesToProduct(productBody.getCategoryIds(), newProduct);
        Product savedProduct = productRepository.save(newProduct);

        String imageUrl = imageService.uploadImage(productBody.getImage());
        if (imageUrl == null) {
            throw new FailedProductCreationException("Failed to upload product image");
        }
        savedProduct.setImage(imageUrl);

        return dtoConverter.getProduct(savedProduct);
    }

    /**
     * Obtiene todos los productos.
     * @return Lista de objetos `ProductResponse` que representan los productos.
     */
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<ProductResponse> products = productRepository.findAll(pageable).map(dtoConverter::getProduct);
        return products.isEmpty() ? Page.empty() : products;
    }

    /**
     * Obtiene un producto por su identificador.
     * @param id Identificador del producto.
     * @return Objeto `ProductResponse` que representa el producto.
     */
    public ProductResponse getProductById(Long id) {
        return dtoConverter.getProduct(entityFinderService.getProductById(id));
    }

    /**
     * Obtiene los productos de una categoría.
     * @param categoryId Identificador de la categoría.
     * @return Lista de objetos `ProductResponse` que representan los productos de la categoría.
     */
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<ProductResponse> products = productRepository.findByCategories_Category_Id(categoryId, pageable)
                .map(dtoConverter::getProduct);
        return products.isEmpty() ? Page.empty() : products;
    }

    /**
     * Actualiza un producto.
     * @param updateProductBody Objeto que contiene la información del producto a actualizar.
     * @return Objeto que contiene la información del producto actualizado.
     */
    @Transactional
    public ProductResponse updateProduct(UpdateProductBody updateProductBody) {

        Product productFound = entityFinderService.getProductById(updateProductBody.getId());

        productFound.setName(updateProductBody.getName());
        productFound.setDescription(updateProductBody.getDescription());
        productFound.setPrice(updateProductBody.getPrice());

        addCategoriesToProduct(updateProductBody.getCategoryIds(), productFound);

        if (updateProductBody.getImage() != null && !updateProductBody.getImage().isEmpty()) {
            String newImageUrl = imageService.uploadImage(updateProductBody.getImage());
            if (newImageUrl != null) {
                imageService.deleteImage(productFound.getImage());
                productFound.setImage(newImageUrl);
            }
        }

        return dtoConverter.getProduct(productFound);
    }

    /**
     * Elimina un producto.
     * @param productId Identificador del producto a eliminar.
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long productId){
        Product product = entityFinderService.getProductById(productId);
        try {
            productRepository.deleteById(product.getId());
            imageService.deleteImage(product.getImage());
        } catch (DataIntegrityViolationException e) {
            throw new ProductException("Cannot delete product with id: " + productId + " due to data integrity constraints", e);
        }
    }

    /**
     * Añade categorías a un producto.
     * @param categoryIds Identificadores de las categorías a añadir.
     * @param product Producto al que se añadirán las categorías.
     */
    @Transactional
    protected void addCategoriesToProduct(List<Long> categoryIds, Product product){

        product.getCategories().clear();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<Product_ProductCategory> productCategories = categoryIds.stream()
                    .map(entityFinderService::getCategoryById)
                    .map(category -> new Product_ProductCategory(product, category))
                    .toList();
            product.getCategories().addAll(productCategories);
        }
    }
}