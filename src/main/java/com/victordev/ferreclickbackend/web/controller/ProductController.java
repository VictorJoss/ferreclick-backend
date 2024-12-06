package com.victordev.ferreclickbackend.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victordev.ferreclickbackend.dto.api.ProductBody;
import com.victordev.ferreclickbackend.dto.api.ProductResponse;
import com.victordev.ferreclickbackend.dto.api.UpdateProductBody;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.repository.CartItemRepository;
import com.victordev.ferreclickbackend.persistence.repository.ProductRepository;
import com.victordev.ferreclickbackend.service.IProductService;
import com.victordev.ferreclickbackend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controlador REST que maneja las peticiones relacionadas con los productos.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * Servicio de productos.
     */
    @Autowired
    private IProductService productService;

    /**
     * Obtiene todos los productos.
     *
     * @return Lista de objetos `ProductResponse` que representan los productos.
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Obtiene un producto por su identificador.
     *
     * @param id Identificador del producto.
     * @return Respuesta HTTP que contiene la información del producto.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo producto.
     *
     * @param name           Nombre del producto.
     * @param description    Descripción del producto.
     * @param price          Precio del producto.
     * @param categoryIdsJson Identificadores de las categorías del producto.
     * @param image          Imagen del producto.
     * @return Respuesta HTTP que indica si el producto ha sido creado correctamente.
     */
    @PreAuthorize("hasAuthority('product:create')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("categoryIds") String categoryIdsJson,
            @RequestParam("image") MultipartFile image) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> categoryIds = objectMapper.readValue(categoryIdsJson, new TypeReference<List<Long>>() {
            });

            ProductBody productBody = new ProductBody();
            productBody.setName(name);
            productBody.setDescription(description);
            productBody.setPrice(price);
            productBody.setCategoryIds(categoryIds);
            productBody.setImage(image);

            ProductResponse savedProduct = productService.createProduct(productBody);
            return ResponseEntity.ok(savedProduct);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Obtiene los productos de una categoría.
     *
     * @param categoryId Identificador de la categoría.
     * @return Lista de objetos `ProductResponse` que representan los productos de la categoría.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            List<ProductResponse> products = productService.getProductsByCategory(categoryId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Actualiza un producto.
     *
     * @param productId      Identificador del producto.
     * @param name           Nombre del producto.
     * @param description    Descripción del producto.
     * @param price          Precio del producto.
     * @param categoryIdsJson Identificadores de las categorías del producto.
     * @param image          Imagen del producto.
     * @return Respuesta HTTP que indica si el producto ha sido actualizado correctamente.
     */
    @PreAuthorize("hasAuthority('product:update')")
    @PutMapping
    public ResponseEntity<ProductResponse> UpdateProduct(
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("categoryIds") String categoryIdsJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> categoryIds = objectMapper.readValue(categoryIdsJson, new TypeReference<List<Long>>() {
            });

            UpdateProductBody updateProductBody = new UpdateProductBody();
            updateProductBody.setId(productId);
            updateProductBody.setName(name);
            updateProductBody.setDescription(description);
            updateProductBody.setPrice(price);
            updateProductBody.setCategoryIds(categoryIds);
            updateProductBody.setImage(image);

            ProductResponse savedProduct = productService.updateProduct(updateProductBody);
            return ResponseEntity.ok(savedProduct);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Elimina un producto.
     *
     * @param productId Identificador del producto.
     * @return Respuesta HTTP que indica si el producto ha sido eliminado correctamente.
     */
    @PreAuthorize("hasAuthority('product:delete')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}