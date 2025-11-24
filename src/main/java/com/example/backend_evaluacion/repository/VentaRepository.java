package com.example.backend_evaluacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend_evaluacion.entity.Venta;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstado(String estado);
}
