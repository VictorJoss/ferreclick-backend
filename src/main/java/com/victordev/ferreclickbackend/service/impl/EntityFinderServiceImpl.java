package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.exceptions.product.ProductNotFoundException;
import com.victordev.ferreclickbackend.exceptions.productCategories.CategoryNotFoundException;
import com.victordev.ferreclickbackend.exceptions.role.TheRoleNotFoundException;
import com.victordev.ferreclickbackend.exceptions.user.UserNotFoundException;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.ProductCategory;
import com.victordev.ferreclickbackend.persistence.entity.Role;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.ProductCategoryRepository;
import com.victordev.ferreclickbackend.persistence.repository.ProductRepository;
import com.victordev.ferreclickbackend.persistence.repository.RoleRepository;
import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import com.victordev.ferreclickbackend.service.IEntityFinderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de búsqueda de entidades que proporciona métodos para obtener usuarios, productos,
 * categorías de productos y roles.
 */
@Service
@RequiredArgsConstructor
public class EntityFinderServiceImpl implements IEntityFinderService {

    /**
     * Repositorio de usuarios.
     */
    private final UserRepository userRepository;
    /**
     * Repositorio de productos.
     */
    private final ProductRepository productRepository;
    /**
     * Repositorio de categorías de productos.
     */
    private final ProductCategoryRepository categoryRepository;
    /**
     * Repositorio de roles.
     */
    private final RoleRepository roleRepository;

    /**
     * Obtiene un usuario por su ID.
     *
     * @param userId ID del usuario.
     * @return Usuario.
     * @throws UserNotFoundException si no se encuentra el usuario.
     */
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param productId ID del producto.
     * @return Producto.
     * @throws ProductNotFoundException si no se encuentra el producto.
     */
    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    /**
     * Obtiene una categoría de productos por su ID.
     *
     * @param categoryId ID de la categoría.
     * @return Categoría de productos.
     * @throws CategoryNotFoundException si no se encuentra la categoría.
     */
    @Override
    public ProductCategory getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
    }

    /**
     * Obtiene un rol por su nombre.
     *
     * @param roleName Nombre del rol.
     * @return Rol.
     * @throws TheRoleNotFoundException si no se encuentra el rol.
     */
    @Override
    public Role getRoleByName(String roleName) {
        if ("ADMIN".equalsIgnoreCase(roleName)) {
            throw new TheRoleNotFoundException("Role 'ADMIN' is not allowed to be retrieved");
        }
        return roleRepository.findByName(Role.RoleEnum.valueOf(roleName))
                .orElseThrow(() -> new TheRoleNotFoundException("Role not found with name: " + roleName));
    }
}