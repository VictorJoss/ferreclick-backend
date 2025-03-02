package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.DTOs.api.analytics.CategoryDistributionDto;
import com.victordev.ferreclickbackend.DTOs.api.analytics.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.DTOs.api.analytics.TopProductDto;
import com.victordev.ferreclickbackend.service.IAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST que maneja las
 * peticiones
 * relacionadas con el análisis de datos.
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    /**
     * Servicio de análisis.
     */
    private final IAnalyticsService analyticsService;

    /**
     * Obtiene los ingresos mensuales.
     *
     * @return Lista de objetos `MonthlyRevenueDto` que representan los ingresos mensuales.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenueDto>> getMonthlyRevenue() {
        return ResponseEntity.ok(analyticsService.getMonthlyRevenue());
    }

    /**
     * Obtiene los productos más añadidos al carrito.
     *
     * @return Lista de objetos `TopProductDto` que representan los productos más añadidos al carrito.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDto>> getMostAddedProductsToCart() {
        return ResponseEntity.ok(analyticsService.getMostAddedProductsToCart());
    }

    /**
     * Obtiene las categorías más añadidas al carrito.
     *
     * @return Lista de objetos `CategoryDistributionDto` que representan las categorías más añadidas al carrito.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/category-distribution")
    public ResponseEntity<List<CategoryDistributionDto>> getMostAddedCategoriesToCart() {
        return ResponseEntity.ok(analyticsService.getMostAddedCategoriesToCart());
    }
}