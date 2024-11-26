package com.victordev.ferreclickbackend.dto.api;

import com.victordev.ferreclickbackend.persistence.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
    List<CartItem> cartItems;
}