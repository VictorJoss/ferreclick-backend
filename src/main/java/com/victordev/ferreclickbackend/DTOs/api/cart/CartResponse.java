package com.victordev.ferreclickbackend.DTOs.api.cart;

import com.victordev.ferreclickbackend.persistence.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Objeto que contiene la información de los productos añadidos al carrito.
 */
@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
    /**
     * Lista de productos añadidos al carrito.
     */
    List<CartItem> cartItems;
}