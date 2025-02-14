package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.ProductBody;
import com.victordev.ferreclickbackend.dto.api.ProductResponse;
import com.victordev.ferreclickbackend.dto.api.UpdateProductBody;
import com.victordev.ferreclickbackend.exceptions.files.InvalidImageException;
import com.victordev.ferreclickbackend.exceptions.product.FailedProductCreationException;
import com.victordev.ferreclickbackend.exceptions.product.ProductAlreadyExistsException;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import com.victordev.ferreclickbackend.persistence.repository.*;
import com.victordev.ferreclickbackend.service.IProductService;
import com.victordev.ferreclickbackend.service.ImageService;
import com.victordev.ferreclickbackend.utils.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de productos que proporciona métodos para crear, obtener, actualizar y eliminar productos.
 */
@Service
public class ProductServiceImpl implements IProductService {

    /**
     * Repositorio de productos.
     */
    @Autowired
    private ProductRepository productRepository;
    /**
     * Repositorio de categorías de productos.
     */
    @Autowired
    private ProductCategoryRepository categoryRepository;
    /**
     * Repositorio de productos en categorías.
     */
    @Autowired
    private Product_ProductCategoryRepository productProductCategoryRepository;
    /**
     * Conversor de DTO.
     */
    @Autowired
    private DtoConverter dtoConverter;
    /**
     * Repositorio de categorías de productos.
     */
    @Autowired
    private ImageService imageService;
    /**
     * Repositorio de categorías de productos.
     */
    @Autowired
    private CartItemRepository cartItemRepository;

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

        if (productBody.getImage().isEmpty() || !Objects.requireNonNull(productBody.getImage().getContentType()).startsWith("image/")) {
            throw new InvalidImageException("The file is not an image");
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
        List<Product> products = productProductCategoryRepository.findProductsByCategoryId(categoryId);
        return products.stream()
                .map(dtoConverter::getProduct)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un producto.
     * @param updateProductBody Objeto que contiene la información del producto a actualizar.
     * @return Objeto que contiene la información del producto actualizado.
     */
    @Transactional
    public ProductResponse updateProduct(UpdateProductBody updateProductBody) {

        Product existingProduct = productRepository.findById(updateProductBody.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + updateProductBody.getId()));

        existingProduct.setName(updateProductBody.getName());
        existingProduct.setDescription(updateProductBody.getDescription());
        existingProduct.setPrice(updateProductBody.getPrice());
        existingProduct.setCategories(new ArrayList<>());
        productProductCategoryRepository.deleteByProduct_Id(existingProduct.getId());

        if(updateProductBody.getImage() != null && !updateProductBody.getImage().isEmpty()){
            imageService.deleteImage(existingProduct.getImage());
            existingProduct.setImage(imageService.uploadImage(updateProductBody.getImage()));
        }

        List<Long> categoryIds = updateProductBody.getCategoryIds();
        addCategoriesToProduct(categoryIds, existingProduct);

        Product savedProduct = productRepository.save(existingProduct);
        return dtoConverter.getProduct(savedProduct);
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
            cartItemRepository.deleteByProduct_Id(product.getId());
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
        if (categoryIds == null || categoryIds.isEmpty()) {
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