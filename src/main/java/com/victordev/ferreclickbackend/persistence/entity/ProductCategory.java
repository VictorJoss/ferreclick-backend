package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Entidad que representa una categoría de productos.
 */
@Entity
@Getter
@Setter
public class ProductCategory {

    /**
     * Identificador de la categoría.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Nombre de la categoría.
     */
    private String name;
    /**
     * Descripción de la categoría.
     */
    private String description;
    /**
     * Productos que pertenecen a la categoría.
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Product_ProductCategory> products;
}