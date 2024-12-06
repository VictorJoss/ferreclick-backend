package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.ProductCategoryBody;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de categorías de productos que proporciona métodos para crear, obtener, actualizar y eliminar
 * categorías de productos.
 */
public interface IProductCategoryService {

    /**
     * Crea una categoría de productos.
     * @param categoryBody Objeto que contiene la información de la categoría de productos.
     * @return Objeto que contiene la información de la categoría de productos creada.
     */
    ProductCategoryBody createCategory(ProductCategoryBody categoryBody);

    /**
     * Obtiene todas las categorías de productos.
     * @return Lista de objetos que contienen la información de las categorías de productos.
     */
    List<ProductCategoryBody> getCategories();

    /**
     * Obtiene una categoría de productos por su identificador.
     * @param id Identificador de la categoría de productos.
     * @return Objeto que contiene la información de la categoría de productos.
     */
    Optional<ProductCategoryBody> getCategoryById(Long id);

    /**
     * Actualiza una categoría de productos.
     * @param categoryBody Objeto que contiene la información de la categoría de productos a actualizar.
     * @return Objeto que contiene la información de la categoría de productos actualizada.
     */
    ProductCategoryBody updateCategory(ProductCategoryBody categoryBody);

    /**
     * Elimina una categoría de productos.
     * @param categoryId Identificador de la categoría de productos.
     */
    void deleteCategory(Long categoryId);
}
