package com.tienda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.backend.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {}
