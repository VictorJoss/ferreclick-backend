package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Objeto que contiene la información de una categoría de productos.
 */
@Getter
@Setter
public class ProductCategoryBody {

    /**
     * Identificador de la categoría.
     */
    private Long id;
    /**
     * Nombre de la categoría.
     */
    private String name;
    /**
     * Descripción de la categoría.
     */
    private String description;
    /**
     * Lista de identificadores de los productos que pertenecen a la categoría.
     */
    private List<Long> productIds;
}
