package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de la entidad `Image`.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
