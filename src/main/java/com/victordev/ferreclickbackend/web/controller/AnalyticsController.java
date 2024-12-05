package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.dto.api.CategoryDistributionDto;
import com.victordev.ferreclickbackend.dto.api.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.dto.api.TopProductDto;
import com.victordev.ferreclickbackend.service.IAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private IAnalyticsService analyticsService;

    @PreAuthorize("permitAll()")
    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenueDto>> getMonthlyRevenue() {
        return ResponseEntity.ok(analyticsService.getMonthlyRevenue());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDto>> getTopSellingProducts() {
        return ResponseEntity.ok(analyticsService.getTopSellingProducts());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/category-distribution")
    public ResponseEntity<List<CategoryDistributionDto>> getCategoryDistribution() {
        return ResponseEntity.ok(analyticsService.getCategoryDistribution());
    }
}