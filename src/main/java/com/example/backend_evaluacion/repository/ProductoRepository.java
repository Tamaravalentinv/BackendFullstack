package com.tienda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.backend.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {}
