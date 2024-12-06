package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.dto.api.AddToCartRequest;
import com.victordev.ferreclickbackend.dto.api.AddedToCartResponse;
import com.victordev.ferreclickbackend.dto.api.CartResponse;
import com.victordev.ferreclickbackend.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que maneja las peticiones relacionadas con el carrito de compras.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    /**
     * Servicio de carrito.
     */
    @Autowired
    private ICartService cartService;

    /**
     * Añade un producto al carrito.
     * @param addToCartRequest Objeto que contiene la información del producto a añadir al carrito.
     * @return Respuesta HTTP que contiene la información del producto añadido al carrito.
     */
    @PreAuthorize("hasAuthority('cart:add-product')")
    @PostMapping("/add-product")
    public ResponseEntity<AddedToCartResponse> addProductToCart(@RequestBody AddToCartRequest addToCartRequest){
        return ResponseEntity.ok(cartService.addProductToCart(addToCartRequest));
    }

    /**
     * Obtiene el carrito de un usuario.
     * @param userId Identificador del usuario.
     * @return Respuesta HTTP que contiene la información del carrito del usuario.
     */
    @PreAuthorize("hasAuthority('cart:read')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    /**
     * Elimina un producto del carrito.
     * @param userId Identificador del usuario.
     * @param productId Identificador del producto.
     * @return Respuesta HTTP que indica si el producto ha sido eliminado correctamente.
     */
    @PreAuthorize("hasAuthority('cart:remove-product')")
    @DeleteMapping("/remove-product/{userId}/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina todos los productos del carrito.
     * @param userId Identificador del usuario.
     * @return Respuesta HTTP que indica si los productos han sido eliminados correctamente.
     */
    @PreAuthorize("hasAuthority('cart:remove-product')")
    @DeleteMapping("/remove-product/all/{userId}")
    public ResponseEntity<Void> removeAllProducts(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Procesa el pago del carrito.
     * @param userId Identificador del usuario.
     * @return Respuesta HTTP que indica si el pago ha sido procesado correctamente.
     */
    @PreAuthorize("hasAuthority('cart:pay')")
    @PostMapping("/pay/{userId}")
    public ResponseEntity<Void> payCart(@PathVariable Long userId) {
        cartService.processPaymentCart(userId);
        return ResponseEntity.noContent().build();
    }
}