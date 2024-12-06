package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.CategoryDistributionDto;
import com.victordev.ferreclickbackend.dto.api.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.dto.api.TopProductDto;

import java.util.List;

public interface IAnalyticsService {

    List<MonthlyRevenueDto> getMonthlyRevenue();

    List<TopProductDto> getMostAddedProductsToCart();

    List<CategoryDistributionDto> getMostAddedCategoriesToCart();
}