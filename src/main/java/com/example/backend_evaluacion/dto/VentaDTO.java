package com.example.backend_evaluacion.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private Long vendedorId;
    private String vendedorNombre;
    private BigDecimal montoTotal;
    private LocalDateTime fecha;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaCreacion;
}
