package com.victordev.ferreclickbackend.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Entidad que representa un producto.
 */
@Entity
@Getter
@Setter
public class Product {

    /**
     * Identificador del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("producId")
    private Long id;
    /**
     * Nombre del producto.
     */
    private String name;
    /**
     * Descripción del producto.
     */
    private String description;
    /**
     * URL de la imagen del producto.
     */
    private String image;
    /**
     * Precio del producto.
     */
    private Double price;
    /**
     * Cantidad disponible del producto.
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Product_ProductCategory> categories;
}