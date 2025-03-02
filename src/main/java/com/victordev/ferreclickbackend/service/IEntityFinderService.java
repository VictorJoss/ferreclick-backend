package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Role;
import com.victordev.ferreclickbackend.persistence.entity.User;

/**
 * Interfaz que proporciona métodos para obtener entidades.
 */
public interface IEntityFinderService {

    /**
     * Obtiene un usuario por su ID.
     * @param userId ID del usuario.
     * @return Usuario.
     */
    User getUserById(Long userId);

    /**
     * Obtiene un producto por su ID.
     * @param productId ID del producto.
     * @return Producto.
     */
    Product getProductById(Long productId);

    /**
     * Obtiene una categoría de productos por su ID.
     * @param categoryId ID de la categoría de productos.
     * @return Categoría de productos.
     */
    ProductCategory getCategoryById(Long categoryId);

    /**
     * Obtiene un rol por su nombre.
     * @param roleName Nombre del rol.
     * @return Rol.
     */
    Role getRoleByName(String roleName);
}
