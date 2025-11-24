package com.example.backend_evaluacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend_evaluacion.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
