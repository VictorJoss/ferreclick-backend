package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.DTOs.api.product.ProductBody;
import com.victordev.ferreclickbackend.DTOs.api.product.ProductResponse;
import com.victordev.ferreclickbackend.DTOs.api.product.UpdateProductBody;
import com.victordev.ferreclickbackend.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * @return Respuesta HTTP que indica si el producto ha sido creado correctamente.
     */
    @PreAuthorize("hasAuthority('product:create')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@ModelAttribute @Valid ProductBody productBody) {

            ProductResponse savedProduct = productService.createProduct(productBody);
            return ResponseEntity.ok(savedProduct);
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
     * @return Respuesta HTTP que indica si el producto ha sido actualizado correctamente.
     */
    @PreAuthorize("hasAuthority('product:update')")
    @PutMapping
    public ResponseEntity<ProductResponse> UpdateProduct(@ModelAttribute @Valid UpdateProductBody updateProductBody) {

            ProductResponse savedProduct = productService.updateProduct(updateProductBody);
            return ResponseEntity.ok(savedProduct);
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