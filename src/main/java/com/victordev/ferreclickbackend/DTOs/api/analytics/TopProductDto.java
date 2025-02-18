package com.victordev.ferreclickbackend.DTOs.api.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto que contiene la información de los productos más vendidos.
 */
@Getter
@Setter
@AllArgsConstructor
public class TopProductDto {

    /**
     * Identificador del producto.
     */
    private String productName;
    /**
     * Cantidad total de unidades vendidas del producto.
     */
    private Long totalSold;
}
