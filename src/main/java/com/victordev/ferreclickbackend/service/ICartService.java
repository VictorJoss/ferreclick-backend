package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.DTOs.api.cart.AddToCartRequest;
import com.victordev.ferreclickbackend.DTOs.api.cart.AddedToCartResponse;
import com.victordev.ferreclickbackend.DTOs.api.cart.CartResponse;
import com.victordev.ferreclickbackend.persistence.entity.Cart;
import com.victordev.ferreclickbackend.persistence.entity.User;

/**
 * Interfaz del servicio de carrito que proporciona métodos para añadir productos al carrito, obtener el carrito
 * de un usuario, eliminar productos del carrito y vaciar el carrito.
 */
public interface ICartService {

    /**
     * Añade un producto al carrito de un usuario.
     * @param addToCartRequest Objeto que contiene la información necesaria para añadir un producto al carrito.
     * @return Objeto que contiene el producto añadido y la cantidad añadida.
     */
    AddedToCartResponse addProductToCart(AddToCartRequest addToCartRequest);

    /**
     * Obtiene el carrito de un usuario.
     * @param userId Identificador del usuario.
     * @return Objeto que contiene el carrito del usuario.
     */
    CartResponse getCartByUserId(Long userId);

    /**
     * Elimina un producto del carrito de un usuario.
     * @param userId Identificador del usuario.
     * @param productId Identificador del producto.
     */
    void removeProductFromCart(Long userId, Long productId);

    /**
     * Crea un carrito para un usuario.
     * @param user Usuario al que se le creará el carrito.
     * @return Objeto que contiene el carrito creado.
     */
    Cart createCart(User user);

    /**
     * Vacía el carrito de un usuario.
     * @param userId Identificador del usuario.
     */
    void clearCart(Long userId);

    /**
     * Procesa el pago del carrito de un usuario.
     * @param userId Identificador del usuario.
     */
    void processPaymentCart(Long userId);
}