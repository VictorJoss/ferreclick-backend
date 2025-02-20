package com.victordev.ferreclickbackend.persistence.repository;

import com.victordev.ferreclickbackend.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de la entidad `User`.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Obtiene el usuario por email.
     * @param email Email del usuario.
     * @return Objeto `User` que representa el usuario.
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Obtiene el usuario por nombre de usuario.
     * @param username Nombre de usuario.
     * @return Objeto `User` que representa el usuario.
     */
    Optional<User> findByUsernameIgnoreCase(String username);
}
