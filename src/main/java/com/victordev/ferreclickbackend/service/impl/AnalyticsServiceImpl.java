package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.CategoryDistributionDto;
import com.victordev.ferreclickbackend.dto.api.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.dto.api.TopProductDto;
import com.victordev.ferreclickbackend.DTOs.api.analytics.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.DTOs.api.analytics.TopProductDto;
import com.victordev.ferreclickbackend.persistence.repository.AnalyticsRepository;
import com.victordev.ferreclickbackend.service.IAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de análisis que proporciona métodos para obtener ingresos mensuales,
 * productos más añadidos al carrito y categorías más añadidas al carrito.
 */
@Service
public class AnalyticsServiceImpl implements IAnalyticsService {

    /**
     * Repositorio de análisis.
     */
    @Autowired
    private AnalyticsRepository analyticsRepository;

    /**
     * Obtiene los ingresos mensuales.
     *
     * @return Lista de objetos `MonthlyRevenueDto` que representan los ingresos mensuales.
     */
    public List<MonthlyRevenueDto> getMonthlyRevenue() {
        List<Object[]> results = analyticsRepository.findMonthlyRevenue();
        return results.stream()
                .map(row -> new MonthlyRevenueDto((Integer) row[0], (Double) row[1]))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los productos más añadidos al carrito.
     *
     * @return Lista de objetos `TopProductDto` que representan los productos más añadidos al carrito.
     */
    public List<TopProductDto> getMostAddedProductsToCart() {
        List<Object[]> results = analyticsRepository.findMostAddedProductsToCart();
        return results.stream()
                .map(row -> new TopProductDto((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

     /**
     * Obtiene las categorías más añadidas al carrito.
     *
     * @return Lista de objetos `CategoryDistributionDto` que representan las categorías más añadidas al carrito.
     */
    public List<CategoryDistributionDto> getMostAddedCategoriesToCart() {
        List<Object[]> results = analyticsRepository.findMostAddedCategoriesToCart();
        return results.stream()
                .map(row -> new CategoryDistributionDto((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }
}
