package com.victordev.ferreclickbackend.DTOs.api.cart;

import com.victordev.ferreclickbackend.persistence.entity.Product;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto que contiene la información de un producto añadido al carrito y la cantidad añadida.
 */
@Getter
@Setter
public class AddedToCartResponse {

    /**
     * Producto añadido al carrito.
     */
    private Product product;
    /**
     * Cantidad añadida al carrito.
     */
    private int quantity;
}
