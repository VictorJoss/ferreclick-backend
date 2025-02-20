package com.victordev.ferreclickbackend.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entidad que representa un producto.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @OneToMany( mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Product_ProductCategory> categories;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CartItem> cartItems;

    public Product(String name, String description, Double price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }
}