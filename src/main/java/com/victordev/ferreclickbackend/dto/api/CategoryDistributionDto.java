package com.victordev.ferreclickbackend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto que contiene la información de la distribución de las categorías más añadidas al carrito.
 */
@Getter
@Setter
@AllArgsConstructor
public class CategoryDistributionDto {

    /**
     * Identificador de la categoría.
     */
    private String categoryName;
    /**
     * Cantidad total de productos añadidos al carrito de la categoría.
     */
    private Long count;
}