package com.example.backend_evaluacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "detalle_ventas")
@Data
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal precioUnitario;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal subtotal;

    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
