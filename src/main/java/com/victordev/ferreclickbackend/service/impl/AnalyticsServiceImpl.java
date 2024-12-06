package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.CategoryDistributionDto;
import com.victordev.ferreclickbackend.dto.api.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.dto.api.TopProductDto;
import com.victordev.ferreclickbackend.persistence.repository.AnalyticsRepository;
import com.victordev.ferreclickbackend.service.IAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements IAnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    public List<MonthlyRevenueDto> getMonthlyRevenue() {
        List<Object[]> results = analyticsRepository.findMonthlyRevenue();
        return results.stream()
                .map(row -> new MonthlyRevenueDto((Integer) row[0], (Double) row[1]))
                .collect(Collectors.toList());
    }

    public List<TopProductDto> getMostAddedProductsToCart() {
        List<Object[]> results = analyticsRepository.findMostAddedProductsToCart();
        return results.stream()
                .map(row -> new TopProductDto((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    public List<CategoryDistributionDto> getMostAddedCategoriesToCart() {
        List<Object[]> results = analyticsRepository.findMostAddedCategoriesToCart();
        return results.stream()
                .map(row -> new CategoryDistributionDto((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }
}
