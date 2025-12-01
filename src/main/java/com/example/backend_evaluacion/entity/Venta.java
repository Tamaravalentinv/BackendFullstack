package com.example.backend_evaluacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = true)
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal montoTotal;

    private LocalDateTime fecha = LocalDateTime.now();

    @Column(length = 20)
    private String estado; // PENDIENTE, COMPLETADA, CANCELADA

    @Column(length = 500)
    private String observaciones;

    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
}
