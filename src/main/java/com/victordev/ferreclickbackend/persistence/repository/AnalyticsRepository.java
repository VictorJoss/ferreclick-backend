package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT MONTH(c.createdDate) AS month, SUM(ci.quantity * p.price) AS revenue " +
            "FROM CartItem ci JOIN ci.product p JOIN ci.cart c " +
            "WHERE c.completed = true " +
            "GROUP BY MONTH(c.createdDate)")
    List<Object[]> findMonthlyRevenue();

    @Query("SELECT p.name, SUM(ci.quantity) AS totalSold " +
            "FROM CartItem ci JOIN ci.product p " +
            "GROUP BY p.name ORDER BY totalSold DESC")
    List<Object[]> findMostAddedProductsToCart();

    @Query("""
    SELECT pc.name AS categoryName, COUNT(ci) AS categoryCount
    FROM CartItem ci
    JOIN ci.product p
    JOIN p.categories pc_p
    JOIN pc_p.category pc
    GROUP BY pc.name
    ORDER BY categoryCount DESC
    """)
    List<Object[]> findMostAddedCategoriesToCart();
}
