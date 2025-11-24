package com.tienda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.backend.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {}
