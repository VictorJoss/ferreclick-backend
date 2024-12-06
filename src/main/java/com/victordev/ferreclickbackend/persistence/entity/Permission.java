package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un permiso.
 */
@Entity
@Getter
@Setter
public class Permission {

    /**
     * Identificador del permiso.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Nombre del permiso.
     */
    private String name;
}