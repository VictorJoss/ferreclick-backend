package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.DTOs.api.productCategory.ProductCategoryBody;
import com.victordev.ferreclickbackend.service.IProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST que maneja las peticiones relacionadas con las categorías de productos.
 */
@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    /**
     * Servicio de categorías de productos.
     */
    @Autowired
    private IProductCategoryService productCategoryService;

    /**
     * Obtiene todas las categorías de productos.
     * @return Lista de objetos `ProductCategoryBody` que representan las categorías de productos.
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<ProductCategoryBody> getAllProductCategories() {
        return productCategoryService.getCategories();
    }

    /**
     * Obtiene una categoría de productos por su identificador.
     * @param id Identificador de la categoría.
     * @return Respuesta HTTP que contiene la información de la categoría.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryBody> getProductCategoryById(@PathVariable Long id) {
        return productCategoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva categoría de productos.
     * @param productCategoryBody Objeto que contiene la información de la categoría a crear.
     * @return Respuesta HTTP que indica si la categoría ha sido creada correctamente.
     */
    @PreAuthorize("hasAuthority('category:create')")
    @PostMapping
    public ResponseEntity<ProductCategoryBody> createProductCategory(@RequestBody @Valid ProductCategoryBody productCategoryBody) {
        ProductCategoryBody createdCategory = productCategoryService.createCategory(productCategoryBody);
        URI location = URI.create("/api/product-categories" + createdCategory.getId());
        return ResponseEntity.created(location).build();
    }

    /**
     * Actualiza una categoría de productos.
     * @param categoryBody Objeto que contiene la información de la categoría a actualizar.
     * @return Respuesta HTTP que contiene la información de la categoría actualizada.
     */
    @PreAuthorize("hasAuthority('category:update')")
    @PutMapping
    public ResponseEntity<ProductCategoryBody> updateCategory(@RequestBody @Valid ProductCategoryBody categoryBody){
        return ResponseEntity.ok(productCategoryService.updateCategory(categoryBody));
    }

    /**
     * Elimina una categoría de productos.
     * @param categoryId Identificador de la categoría.
     * @return Respuesta HTTP que indica si la categoría ha sido eliminada correctamente.
     */
    @PreAuthorize("hasAuthority('category:delete')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId){
        productCategoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
