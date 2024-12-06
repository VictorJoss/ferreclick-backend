package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa una relación entre un rol y un permiso.
 */
@Entity
@Getter
@Setter
public class Role_Permission {

    /**
     * Identificador de la relación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Rol que contiene la relación.
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    /**
     * Permiso que contiene la relación.
     */
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}