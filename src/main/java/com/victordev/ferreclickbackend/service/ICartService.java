package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.AddToCartRequest;
import com.victordev.ferreclickbackend.dto.api.AddedToCartResponse;
import com.victordev.ferreclickbackend.dto.api.CartResponse;
import com.victordev.ferreclickbackend.persistence.entity.Cart;
import com.victordev.ferreclickbackend.persistence.entity.User;


public interface ICartService {
    AddedToCartResponse addProductToCart(AddToCartRequest addToCartRequest);
    CartResponse getCartByUserId(Long userId);
    void removeProductFromCart(Long userId, Long productId);
    Cart createCart(User user);
    void clearCart(Long userId);
}