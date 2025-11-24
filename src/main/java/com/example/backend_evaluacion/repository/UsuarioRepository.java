package com.example.backend_evaluacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend_evaluacion.entity.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
