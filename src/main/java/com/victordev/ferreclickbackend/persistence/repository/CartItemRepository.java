package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de la entidad `CartItem`.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * Obtiene los elementos del carrito de un usuario.
     * @param userId Identificador del usuario.
     * @return Lista de objetos `CartItem` que representan los elementos del carrito.
     */
    List<CartItem> findByCart_Id(Long userId);

    /**
     * Elimina los elementos por productId del carrito de un usuario.
     * @param productId
     */
    void deleteByProduct_Id(Long productId);

    /**
     * Elimina todos los elementos del carrito de un usuario.
     * @param cartId Identificador del carrito.
     */
    void deleteAllByCart_Id(Long cartId);
}