package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.DTOs.api.product.ProductBody;
import com.victordev.ferreclickbackend.DTOs.api.product.ProductResponse;
import com.victordev.ferreclickbackend.DTOs.api.product.UpdateProductBody;
import com.victordev.ferreclickbackend.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que maneja las peticiones relacionadas con los productos.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    /**
     * Servicio de productos.
     */
    private final IProductService productService;

    /**
     * Obtiene todos los productos.
     *
     * @return Lista de objetos `ProductResponse` que representan los productos.
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductResponse> products = productService.getAllProducts(PageRequest.of(page, size));
        return ResponseEntity.ok(products);
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
        return ResponseEntity.ok(productService.getProductById(id));
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
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
            Page<ProductResponse> products = productService.getProductsByCategory(categoryId, PageRequest.of(page, size));
            return ResponseEntity.ok(products);
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