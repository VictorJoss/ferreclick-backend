package com.victordev.ferreclickbackend.dto.api;

import com.victordev.ferreclickbackend.persistence.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddedToCartResponse {

    private Product product;
    private int quantity;
}
