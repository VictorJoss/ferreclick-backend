package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa una relación entre un producto y una categoría.
 */
@Entity
@Getter
@Setter
public class Product_ProductCategory {

    /**
     * Identificador de la relación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Producto que contiene la relación.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    /**
     * Categoría que contiene la relación.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    /**
     * Constructor que permite crear una relación entre un producto y una categoría.
     *
     * @param product  Producto que contiene la relación.
     * @param category Categoría que contiene la relación.
     */
    public Product_ProductCategory(Product product, ProductCategory category) {
        this.product = product;
        this.category = category;
    }

    /**
     * Constructor vacío.
     */
    public Product_ProductCategory() {
    }
}