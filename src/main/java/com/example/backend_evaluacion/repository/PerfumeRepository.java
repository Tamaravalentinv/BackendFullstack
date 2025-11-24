package com.example.backend_evaluacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend_evaluacion.entity.Perfume;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
}
