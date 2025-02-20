package com.victordev.ferreclickbackend.DTOs.api.productCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @Positive
    private Long id;
    /**
     * Nombre de la categoría.
     */
    @NotNull
    @NotBlank
    private String name;
    /**
     * Descripción de la categoría.
     */
    @NotNull
    @NotBlank
    private String description;
    /**
     * Lista de identificadores de los productos que pertenecen a la categoría.
     */
    private List<Long> productIds;
}
