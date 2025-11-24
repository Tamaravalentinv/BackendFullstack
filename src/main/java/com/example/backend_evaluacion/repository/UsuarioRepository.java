package com.tienda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.backend.entity.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
