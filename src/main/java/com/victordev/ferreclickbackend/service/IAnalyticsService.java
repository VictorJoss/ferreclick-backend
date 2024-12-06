package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.CategoryDistributionDto;
import com.victordev.ferreclickbackend.dto.api.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.dto.api.TopProductDto;

import java.util.List;

/**
 * Interfaz del servicio de análisis que proporciona métodos para obtener ingresos mensuales,
 * productos más añadidos al carrito y categorías más añadidas al carrito.
 */
public interface IAnalyticsService {

    /**
     * Obtiene los ingresos mensuales.
     *
     * @return Lista de objetos `MonthlyRevenueDto` que representan los ingresos mensuales.
     */
    List<MonthlyRevenueDto> getMonthlyRevenue();

    /**
     * Obtiene los productos más añadidos al carrito.
     *
     * @return Lista de objetos `TopProductDto` que representan los productos más añadidos al carrito.
     */
    List<TopProductDto> getMostAddedProductsToCart();

    /**
     * Obtiene las categorías más añadidas al carrito.
     *
     * @return Lista de objetos `CategoryDistributionDto` que representan las categorías más añadidas al carrito.
     */
    List<CategoryDistributionDto> getMostAddedCategoriesToCart();
}