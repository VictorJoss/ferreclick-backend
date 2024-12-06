package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de la entidad `Role`.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Obtiene el rol por nombre.
     * @param roleEnum Nombre del rol.
     * @return Objeto `Role` que representa el rol.
     */
    Optional<Role> findByName(Role.RoleEnum roleEnum);
}
