package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
