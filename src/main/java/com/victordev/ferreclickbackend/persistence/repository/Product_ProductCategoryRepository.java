package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Product_ProductCategoryRepository extends JpaRepository<Product_ProductCategory, Long> {

    @Query("SELECT p.product FROM Product_ProductCategory p WHERE p.category.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);
    void deleteByProduct_Id(Long id);
    void deleteByCategory_Id(Long id);
}
