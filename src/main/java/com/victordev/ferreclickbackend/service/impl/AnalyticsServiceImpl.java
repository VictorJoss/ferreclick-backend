package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.analytics.CategoryDistributionDto;
import com.victordev.ferreclickbackend.DTOs.api.analytics.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.DTOs.api.analytics.TopProductDto;
import com.victordev.ferreclickbackend.exceptions.Analytics.AnalyticsDataNotFoundException;
import com.victordev.ferreclickbackend.exceptions.Analytics.AnalyticsDatabaseException;
import com.victordev.ferreclickbackend.exceptions.Analytics.AnalyticsException;
import com.victordev.ferreclickbackend.persistence.repository.AnalyticsRepository;
import com.victordev.ferreclickbackend.service.IAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de análisis que proporciona métodos para obtener ingresos mensuales,
 * productos más añadidos al carrito y categorías más añadidas al carrito.
 */
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements IAnalyticsService {

    /**
     * Repositorio de análisis.
     */
    private final AnalyticsRepository analyticsRepository;

    /**
     * Obtiene los ingresos mensuales.
     *
     * @return Lista de objetos `MonthlyRevenueDto` que representan los ingresos mensuales.
     */
    @Override
    public List<MonthlyRevenueDto> getMonthlyRevenue() {
        try {
            List<Object[]> results = analyticsRepository.findMonthlyRevenue();
            if (results.isEmpty()) {
                throw new AnalyticsDataNotFoundException("No monthly revenue data available.");
            }
            return results.stream()
                    .map(row -> new MonthlyRevenueDto((Integer) row[0], (Double) row[1]))
                    .toList();
        } catch (DataAccessException e) {
            throw new AnalyticsDatabaseException("Error accessing database while fetching monthly revenue", e);
        } catch (Exception e) {
            throw new AnalyticsException("Unexpected error occurred while fetching monthly revenue", e);
        }
    }

    /**
     * Obtiene los productos más añadidos al carrito.
     *
     * @return Lista de objetos `TopProductDto` que representan los productos más añadidos al carrito.
     */
    @Override
    public List<TopProductDto> getMostAddedProductsToCart() {
        try {
            List<Object[]> results = analyticsRepository.findMostAddedProductsToCart();
            if (results.isEmpty()) {
                throw new AnalyticsDataNotFoundException("No top products data available.");
            }
            return results.stream()
                    .map(row -> new TopProductDto((String) row[0], (Long) row[1]))
                    .toList();
        } catch (DataAccessException e) {
            throw new AnalyticsDatabaseException("Error accessing database while fetching top products", e);
        } catch (Exception e) {
            throw new AnalyticsException("Unexpected error occurred while fetching top products", e);
        }
    }

     /**
     * Obtiene las categorías más añadidas al carrito.
     *
     * @return Lista de objetos `CategoryDistributionDto` que representan las categorías más añadidas al carrito.
     */
     @Override
     public List<CategoryDistributionDto> getMostAddedCategoriesToCart() {
         try {
             List<Object[]> results = analyticsRepository.findMostAddedCategoriesToCart();
             if (results.isEmpty()) {
                 throw new AnalyticsDataNotFoundException("No category distribution data available.");
             }
             return results.stream()
                     .map(row -> new CategoryDistributionDto((String) row[0], (Long) row[1]))
                     .toList();
         } catch (DataAccessException e) {
             throw new AnalyticsDatabaseException("Error accessing database while fetching category distribution", e);
         } catch (Exception e) {
             throw new AnalyticsException("Unexpected error occurred while fetching category distribution", e);
         }
     }
}
