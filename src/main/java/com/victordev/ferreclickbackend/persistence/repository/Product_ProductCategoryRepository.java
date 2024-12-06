package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.Product_ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de la entidad `Product_ProductCategory`.
 */
@Repository
public interface Product_ProductCategoryRepository extends JpaRepository<Product_ProductCategory, Long> {

    /**
     * Obtiene los productos por categoría.
     * @param categoryId Identificador de la categoría.
     * @return Lista de objetos `Product` que representan los productos de la categoría.
     */
    @Query("SELECT p.product FROM Product_ProductCategory p WHERE p.category.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);
    void deleteByProduct_Id(Long id);
    void deleteByCategory_Id(Long id);
}
