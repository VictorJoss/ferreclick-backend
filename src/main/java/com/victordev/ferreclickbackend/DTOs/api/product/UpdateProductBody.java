package com.victordev.ferreclickbackend.DTOs.api.product;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Objeto que contiene la información de un producto.
 */
@Getter
@Setter
public class UpdateProductBody {

    /**
     * Identificador del producto.
     */
    @NotNull
    @Positive
    private Long id;
    /**
     * Nombre del producto.
     */
    @NotNull
    @NotBlank
    private String name;
    /**
     * Descripción del producto.
     */
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String description;
    /**
     * Imagen del producto.
     */
    private MultipartFile image;
    /**
     * Precio del producto.
     */
    @NotNull
    @Positive
    private Double price;
    /**
     * Lista de identificadores de las categorías a las que pertenece el producto.
     */
    @NotNull
    @NotEmpty
    private List<Long> categoryIds;
}
