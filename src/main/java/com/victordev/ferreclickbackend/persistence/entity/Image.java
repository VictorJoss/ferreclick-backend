package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa una imagen.
 */
@Entity
@Setter
@Getter
public class Image {

    /**
     * Identificador de la imagen.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * URL de la imagen.
     */
    @Column(name = "url_image")
    private String url;
}