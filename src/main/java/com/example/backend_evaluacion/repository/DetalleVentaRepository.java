package com.example.backend_evaluacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend_evaluacion.entity.DetalleVenta;
import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVentaId(Long ventaId);
}
