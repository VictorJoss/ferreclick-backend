package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.dto.api.AddToCartRequest;
import com.victordev.ferreclickbackend.dto.api.AddedToCartResponse;
import com.victordev.ferreclickbackend.dto.api.CartResponse;
import com.victordev.ferreclickbackend.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PreAuthorize("hasAuthority('cart:add-product')")
    @PostMapping("/add-product")
    public ResponseEntity<AddedToCartResponse> addProductToCart(@RequestBody AddToCartRequest addToCartRequest){
        return ResponseEntity.ok(cartService.addProductToCart(addToCartRequest));
    }

    @PreAuthorize("hasAuthority('cart:read')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PreAuthorize("hasAuthority('cart:remove-product')")
    @DeleteMapping("/remove-product/{userId}/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }
}