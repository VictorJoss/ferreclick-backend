package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto que contiene la información de un producto añadido al carrito y la cantidad añadida.
 */
@Getter
@Setter
public class AddToCartRequest {

    /**
     * Identificador del usuario que añadirá el producto al carrito.
     */
    private Long userId;
    /**
     * Identificador del producto que se añadirá al carrito.
     */
    private Long productId;
    /**
     * Cantidad que se añadirá al carrito.
     */
    private int quantity;
}
