package com.example.backend_evaluacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend_evaluacion.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
