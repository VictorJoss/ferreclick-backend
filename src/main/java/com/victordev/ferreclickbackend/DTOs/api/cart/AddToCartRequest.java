package com.victordev.ferreclickbackend.DTOs.api.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull
    @Positive
    private Long userId;
    /**
     * Identificador del producto que se añadirá al carrito.
     */
    @NotNull
    @Positive
    private Long productId;
    /**
     * Cantidad que se añadirá al carrito.
     */
    @NotNull
    @Positive
    private int quantity;
}
