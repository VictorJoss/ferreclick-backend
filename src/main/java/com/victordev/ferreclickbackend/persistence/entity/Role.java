package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Entidad que representa un rol.
 */
@Entity
@Getter
@Setter
public class Role implements GrantedAuthority {

    /**
     * Identificador del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del rol.
     */
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    /**
     * Descripción del rol.
     */
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role_Permission> permissions;

    /**
     * Lista de usuarios que tienen el rol.
     */
    @Override
    public String getAuthority() {
        if(name == null){
            return null;
        }
        return "ROLE_" + name.name();
    }

    /**
     * Enumeración que representa los roles disponibles.
     */
    public static enum RoleEnum{
        CUSTOMER,
        ADMIN
    }
}