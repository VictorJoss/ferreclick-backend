package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de la entidad `ProductCategory`.
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    /**
     * Obtiene la categoría por nombre.
     * @param name Nombre de la categoría.
     * @return Objeto `ProductCategory` que representa la categoría.
     */
    Optional<ProductCategory> findByNameIgnoreCase(String name);
}
