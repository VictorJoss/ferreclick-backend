package com.victordev.ferreclickbackend.DTOs.api.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Objeto que contiene la información de un producto.
 */
@Getter
@Setter
public class ProductResponse {

    /**
     * Identificador del producto.
     */
    private Long id;
    /**
     * Nombre del producto.
     */
    private String name;
    /**
     * Descripción del producto.
     */
    private String description;
    /**
     * URL de la imagen del producto.
     */
    private String image;
    /**
     * Precio del producto.
     */
    private Double price;
    /**
     * Lista de identificadores de las categorías a las que pertenece el producto.
     */
    private List<Long> categoryIds;
}
