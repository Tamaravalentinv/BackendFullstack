package com.example.backend_evaluacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend_evaluacion.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
