package com.victordev.ferreclickbackend.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un ítem de un carrito de compras.
 */
@Entity
@Setter
@Getter
public class CartItem {

    /**
     * Identificador del ítem.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Producto que contiene el ítem.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    /**
     * Carrito al que pertenece el ítem.
     */
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;
    /**
     * Cantidad del producto que contiene el ítem.
     */
    private int quantity;
}