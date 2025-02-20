package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de la entidad `Product`.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Obtiene el producto por nombre.
     * @param name Nombre del producto.
     * @return Objeto `Product` que representa el producto.
     */
    Optional<Product> findByNameIgnoreCase(String name);
}
