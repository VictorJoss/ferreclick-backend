package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.ProductBody;
import com.victordev.ferreclickbackend.dto.api.ProductResponse;
import com.victordev.ferreclickbackend.dto.api.UpdateProductBody;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de productos que proporciona métodos para crear, obtener, actualizar y eliminar productos.
 */
public interface IProductService {

    /**
     * Crea un producto.
     * @param productBody Objeto que contiene la información del producto.
     * @return Objeto que contiene la información del producto creado.
     */
    ProductResponse createProduct(ProductBody productBody);

    /**
     * Obtiene todos los productos.
     * @return Lista de objetos que contienen la información de los productos.
     */
    List<ProductResponse> getAllProducts();

    /**
     * Obtiene un producto por su identificador.
     * @param id Identificador del producto.
     * @return Objeto que contiene la información del producto.
     */
    Optional<ProductResponse> getProductById(Long id);

    /**
     * Actualiza un producto.
     * @param updateProductBody Objeto que contiene la información del producto a actualizar.
     * @return Objeto que contiene la información del producto actualizado.
     */
    ProductResponse updateProduct(UpdateProductBody updateProductBody);

    /**
     * Obtiene los productos de una categoría.
     * @param categoryId Identificador de la categoría.
     * @return Lista de objetos que contienen la información de los productos.
     */
    List<ProductResponse> getProductsByCategory(Long categoryId);

    /**
     * Elimina un producto.
     * @param productId Identificador del producto.
     */
    void deleteProduct(Long productId);
}