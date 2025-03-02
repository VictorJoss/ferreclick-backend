package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Entidad que representa un usuario.
 */
@Entity
@Getter
@Setter
public class User implements UserDetails {

    /**
     * Identificador del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Nombre del usuario.
     */
    private String name;
    /**
     * Apellido del usuario.
     */
    private String username;
    /**
     * Correo electrónico del usuario.
     */
    private String email;
    /**
     * Contraseña del usuario.
     */
    private String password;
    /**
     * Rol del usuario.
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    /**
     * Carritos del usuario.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts;
    /**
     * Indica si la cuenta del usuario ha expirado.
     */
    private boolean accountExpired;
    /**
     * Indica si la cuenta del usuario ha sido bloqueada.
     */
    private boolean accountLocked;
    /**
     * Indica si las credenciales del usuario han expirado.
     */
    private boolean credentialsExpired;
    /**
     * Indica si el usuario está habilitado.
     */
    private boolean enabled;

    /**
     * Lista de roles del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == null){
            return null;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.getAuthority()));

        if(this.role.getPermissions() == null){
            return authorities;
        }

        role.getPermissions().stream()
                .forEach(each -> {
                    String permissionName = each.getPermission().getName();
                    authorities.add(new SimpleGrantedAuthority(permissionName));
                });
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Obtiene la contraseña del usuario.
     */
    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    /**
     * Indica si la cuenta del usuario ha sido bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    /**
     * Indica si las credenciales del usuario han expirado.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    /**
     * Indica si el usuario está habilitado.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}