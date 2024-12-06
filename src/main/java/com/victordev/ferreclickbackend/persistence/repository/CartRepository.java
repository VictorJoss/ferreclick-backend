package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de la entidad `Cart`.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Obtiene el carrito de un usuario.
     * @param id Identificador del usuario.
     * @return Objeto `Cart` que representa el carrito del usuario.
     */
    Optional<Cart> findById(Long id);
}