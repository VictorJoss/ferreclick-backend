package com.victordev.ferreclickbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    private List<Role_Permission> permissions;

    @Override
    public String getAuthority() {
        if(name == null){
            return null;
        }
        return "ROLE_" + name.name();
    }

    public static enum RoleEnum{
        CUSTOMER,
        ADMIN
    }
}