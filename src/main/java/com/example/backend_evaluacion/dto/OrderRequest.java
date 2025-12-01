package com.example.backend_evaluacion.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    private Long clienteId;
    private Long vendedorId;
    private BigDecimal montoTotal;
    private String estado;
    // Campos opcionales de trazabilidad
    private String medioPago;
    private Integer cuotas;
    // Detalle de productos
    private List<DetalleRequest> detalle;
}
