package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un carrito de compras.
 */
@Entity
@Setter
@Getter
public class Cart {

    /**
     * Identificador del carrito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Usuario al que pertenece el carrito.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * Lista de productos que contiene el carrito.
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    private List<CartItem> items;
    /**
     * Fecha y hora en la que se creó el carrito.
     */
    private LocalDateTime createdDate;
    /**
     * Indica si el carrito ha sido completado.
     */
    private boolean completed;
}