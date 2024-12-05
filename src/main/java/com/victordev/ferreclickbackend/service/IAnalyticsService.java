package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.CategoryDistributionDto;
import com.victordev.ferreclickbackend.dto.api.MonthlyRevenueDto;
import com.victordev.ferreclickbackend.dto.api.TopProductDto;

import java.util.List;
import java.util.stream.Collectors;

public interface IAnalyticsService {

    List<MonthlyRevenueDto> getMonthlyRevenue();

    List<TopProductDto> getTopSellingProducts();

    List<CategoryDistributionDto> getCategoryDistribution();
}