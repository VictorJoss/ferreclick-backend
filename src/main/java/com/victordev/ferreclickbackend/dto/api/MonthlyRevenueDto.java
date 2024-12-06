package com.victordev.ferreclickbackend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto que contiene la información de los ingresos mensuales.
 */
@Getter
@Setter
@AllArgsConstructor
public class MonthlyRevenueDto {

    /**
     * Mes al que corresponde el ingreso.
     */
    private Integer month;
    /**
     * Año al que corresponde el ingreso.
     */
    private Double revenue;
}
