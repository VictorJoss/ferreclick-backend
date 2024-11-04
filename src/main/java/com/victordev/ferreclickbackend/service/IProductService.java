package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.persistence.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductService {

    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product addProduct(Product product);
}
