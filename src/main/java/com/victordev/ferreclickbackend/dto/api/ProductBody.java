package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Objeto que contiene la información de un producto.
 */
@Getter
@Setter
public class ProductBody {

    /**
     * Nombre del producto.
     */
    private String name;
    /**
     * Descripción del producto.
     */
    private String description;
    /**
     * Imagen del producto.
     */
    private MultipartFile image;
    /**
     * Precio del producto.
     */
    private Double price;
    /**
     * Lista de identificadores de las categorías a las que pertenece el producto.
     */
    private List<Long> categoryIds;
}
